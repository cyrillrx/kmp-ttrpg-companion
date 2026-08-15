package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.cyrillrx.rpg.cache.AppDatabase
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AppDatabaseMigrationTest {

    @Test
    fun `migrating an existing database reaches the canonical schema`() {
        val canonical = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also { AppDatabase.Schema.create(it) }
        val migrated = migratedFromV1()

        MIGRATED_TABLES.forEach { table ->
            val expected = columnsOf(canonical, table)
            // PRAGMA table_info answers an unknown table with zero rows, so without this the
            // comparison below would pass by comparing two empty sets.
            assertTrue(expected.isNotEmpty(), "$table is missing from the canonical schema")
            assertEquals(
                expected = expected,
                actual = columnsOf(migrated, table),
                message = "schema mismatch on $table",
            )
        }
    }

    @Test
    fun `migrating from v1 preserves existing rows and defaults palette to arcane`() {
        val preferences = AppDatabase(migratedFromV1()).appDatabaseQueries
            .getUserPreferences { _, theme, palette, distanceUnit -> Triple(theme, palette, distanceUnit) }
            .executeAsOne()

        assertEquals("arcane", preferences.second)
        assertEquals("dark", preferences.first)
        assertEquals("meters", preferences.third)
    }

    @Test
    fun `migrating from v2 carries the user list lastModified over to updatedAt`() {
        val driver = v2Database()
        driver.execute(
            null,
            "INSERT INTO UserList (id, name, type, itemIds, lastModified) " +
                "VALUES ('list-1', 'Combat Spells', 'SPELL', 'Fireball|Thunderwave', 1700000000000);",
            0,
        )
        AppDatabase.Schema.migrate(driver, oldVersion = 2L, newVersion = AppDatabase.Schema.version)

        val lists = AppDatabase(driver).appDatabaseQueries
            .selectAllUserCollectionsByType("SPELL") { _, name, _, itemIds, updatedAt ->
                Triple(name, itemIds, updatedAt)
            }
            .executeAsList()

        assertEquals(1, lists.size)
        assertEquals("Combat Spells", lists.first().first)
        assertEquals("Fireball|Thunderwave", lists.first().second)
        assertEquals(1_700_000_000_000L, lists.first().third)
    }

    @Test
    fun `migrating from v2 preserves characters and defaults updatedAt to zero`() {
        val driver = v2Database()
        driver.execute(null, "INSERT INTO Character (id, data) VALUES ('char-1', '{\"name\":\"Aldus\"}');", 0)
        AppDatabase.Schema.migrate(driver, oldVersion = 2L, newVersion = AppDatabase.Schema.version)

        val characters = AppDatabase(driver).appDatabaseQueries
            .selectAllCharacters { id, data, updatedAt -> Triple(id, data, updatedAt) }
            .executeAsList()

        assertEquals(1, characters.size)
        assertEquals("char-1", characters.first().first)
        assertEquals("{\"name\":\"Aldus\"}", characters.first().second)
        assertEquals(0L, characters.first().third)
    }

    @Test
    fun `migrating from v3 renames the user list table and preserves its rows`() {
        val driver = v3Database()
        driver.execute(
            null,
            "INSERT INTO UserList (id, name, type, itemIds, updatedAt) " +
                "VALUES ('list-1', 'Combat Spells', 'SPELL', 'Fireball|Thunderwave', 1700000000000);",
            0,
        )
        AppDatabase.Schema.migrate(driver, oldVersion = 3L, newVersion = AppDatabase.Schema.version)

        val collections = AppDatabase(driver).appDatabaseQueries
            .selectAllUserCollectionsByType("SPELL") { id, name, _, itemIds, updatedAt ->
                listOf(id, name, itemIds, updatedAt.toString())
            }
            .executeAsList()

        assertEquals(1, collections.size)
        assertEquals(
            listOf("list-1", "Combat Spells", "Fireball|Thunderwave", "1700000000000"),
            collections.first(),
        )
        assertTrue(columnsOf(driver, "UserList").isEmpty(), "UserList should have been renamed, not copied")
    }

    @Test
    fun `an unstamped database is reported as version zero`() {
        assertEquals(0L, JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).schemaVersion())
    }

    @Test
    fun `a stamped database reports the version it was stamped with`() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also { AppDatabase.Schema.create(it) }
        driver.execute(null, "PRAGMA user_version = ${AppDatabase.Schema.version}", 0)

        assertEquals(AppDatabase.Schema.version, driver.schemaVersion())
    }

    @Test
    fun `an unstamped database reports zero whatever it already holds`() {
        assertEquals(0L, v3Database().schemaVersion())
    }

    @Test
    fun `reconciling an unstamped database creates the schema and stamps it`() {
        val driver = JdbcSqliteDriver(fileUrl())

        driver.reconcileSchema()

        assertEquals(AppDatabase.Schema.version, driver.schemaVersion())
        MIGRATED_TABLES.forEach { table ->
            assertTrue(columnsOf(driver, table).isNotEmpty(), "$table is missing after create")
        }
    }

    @Test
    fun `reconciling a stamped older database migrates it and restamps it`() {
        val driver = v3Database(fileUrl()).stampedAt(3L)

        driver.reconcileSchema()

        assertEquals(AppDatabase.Schema.version, driver.schemaVersion())
        assertTrue(columnsOf(driver, "UserCollection").isNotEmpty(), "the v3 to v4 rename did not run")
    }

    @Test
    fun `reconciling a database from a newer build leaves its stamp alone`() {
        val ahead = AppDatabase.Schema.version + 1
        val driver = JdbcSqliteDriver(fileUrl())
            .also { AppDatabase.Schema.create(it) }
            .stampedAt(ahead)

        driver.reconcileSchema()

        assertEquals(ahead, driver.schemaVersion())
    }

    @Test
    fun `a migration failing halfway leaves the database at the version it started from`() {
        // The state an interrupted v2 migration leaves behind: 2.sqm created its transit table before
        // dying, so replaying it fails on `table UserListMigration already exists`.
        val driver = v2Database(fileUrl()).stampedAt(2L)
        driver.execute(null, V2_TRANSIT_TABLE, 0)

        assertFailsWith<Exception> { driver.reconcileSchema() }

        assertEquals(2L, driver.schemaVersion())
        // 2.sqm adds this column before reaching the transit table, so its absence is what proves
        // the whole sequence rolled back rather than only the stamp being withheld.
        assertTrue(
            columnsOf(driver, "Character").none { it.startsWith("updatedAt ") },
            "the partially applied migration was not rolled back",
        )
    }

    private fun SqlDriver.stampedAt(version: Long): SqlDriver =
        also { it.execute(null, "PRAGMA user_version = $version", 0) }

    /**
     * URL of a fresh file-backed database, the shape a real launch opens. The in-memory URL hands
     * the driver a single connection it never closes, so anything relying on state that only lives
     * on one connection — a transaction — passes there whether or not it holds on disk.
     */
    private fun fileUrl(): String {
        val file = File.createTempFile("reconcile", ".db").apply { deleteOnExit() }
        return DesktopDatabaseDriverFactory.URL_PREFIX + file.absolutePath
    }

    /** Database shaped as the schema released in v1, seeded with one preferences row. */
    private fun v1Database(url: String = JdbcSqliteDriver.IN_MEMORY): SqlDriver {
        val driver = JdbcSqliteDriver(url)
        listOf(V1_USER_PREFERENCES, V1_CAMPAIGN, V1_CHARACTER, V1_USER_LIST).forEach { driver.execute(null, it, 0) }
        driver.execute(
            null,
            "INSERT INTO UserPreferencesEntity (id, theme, distance_unit) VALUES (1, 'dark', 'meters');",
            0,
        )
        return driver
    }

    /** Database shaped as the schema released in v2, i.e. v1 plus the palette column. */
    private fun v2Database(url: String = JdbcSqliteDriver.IN_MEMORY): SqlDriver = v1Database(url).also {
        AppDatabase.Schema.migrate(it, oldVersion = 1L, newVersion = 2L)
    }

    /** Database shaped as the schema released in v3, i.e. v2 plus `updatedAt` on lists and characters. */
    private fun v3Database(url: String = JdbcSqliteDriver.IN_MEMORY): SqlDriver = v2Database(url).also {
        AppDatabase.Schema.migrate(it, oldVersion = 2L, newVersion = 3L)
    }

    private fun migratedFromV1(): SqlDriver = v1Database().also {
        AppDatabase.Schema.migrate(it, oldVersion = 1L, newVersion = AppDatabase.Schema.version)
    }

    private fun columnsOf(driver: SqlDriver, table: String): Set<String> = driver.executeQuery(
        null,
        "PRAGMA table_info($table)",
        { cursor ->
            val columns = mutableSetOf<String>()
            while (cursor.next().value) {
                columns += "${cursor.getString(1)} ${cursor.getString(2)} " +
                    "notnull=${cursor.getLong(3)} default=${cursor.getString(4)}"
            }
            QueryResult.Value(columns)
        },
        0,
    ).value

    private companion object {
        val MIGRATED_TABLES = listOf("UserPreferencesEntity", "Campaign", "Character", "UserCollection")

        val V1_USER_PREFERENCES =
            """
            CREATE TABLE UserPreferencesEntity (
                id INTEGER NOT NULL PRIMARY KEY DEFAULT 1,
                theme TEXT NOT NULL DEFAULT 'system',
                distance_unit TEXT NOT NULL DEFAULT 'feet'
            );
            """.trimIndent()

        val V1_CAMPAIGN =
            """
            CREATE TABLE Campaign (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                ruleSet INTEGER NOT NULL
            );
            """.trimIndent()

        val V1_CHARACTER =
            """
            CREATE TABLE Character (
                id TEXT NOT NULL PRIMARY KEY,
                data TEXT NOT NULL
            );
            """.trimIndent()

        /** The transit table 2.sqm creates, as an interrupted run would have left it. */
        val V2_TRANSIT_TABLE =
            """
            CREATE TABLE UserListMigration (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                itemIds TEXT NOT NULL DEFAULT '',
                updatedAt INTEGER NOT NULL DEFAULT 0
            );
            """.trimIndent()

        val V1_USER_LIST =
            """
            CREATE TABLE UserList (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                itemIds TEXT NOT NULL DEFAULT '',
                lastModified INTEGER NOT NULL DEFAULT 0
            );
            """.trimIndent()
    }
}
