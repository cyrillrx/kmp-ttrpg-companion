package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.cyrillrx.rpg.cache.AppDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class AppDatabaseMigrationTest {

    @Test
    fun `migrating an existing database reaches the canonical schema`() {
        val canonical = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also { AppDatabase.Schema.create(it) }

        assertEquals(
            userPreferencesColumns(canonical),
            userPreferencesColumns(migratedFromV1()),
        )
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

    private fun migratedFromV1(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        driver.execute(null, V1_USER_PREFERENCES, 0)
        driver.execute(
            null,
            "INSERT INTO UserPreferencesEntity (id, theme, distance_unit) VALUES (1, 'dark', 'meters');",
            0,
        )
        AppDatabase.Schema.migrate(driver, oldVersion = 1L, newVersion = AppDatabase.Schema.version)
        return driver
    }

    private fun userPreferencesColumns(driver: SqlDriver): Set<String> = driver.executeQuery(
        null,
        "PRAGMA table_info(UserPreferencesEntity)",
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
        val V1_USER_PREFERENCES =
            """
            CREATE TABLE UserPreferencesEntity (
                id INTEGER NOT NULL PRIMARY KEY DEFAULT 1,
                theme TEXT NOT NULL DEFAULT 'system',
                distance_unit TEXT NOT NULL DEFAULT 'feet'
            );
            """.trimIndent()
    }
}
