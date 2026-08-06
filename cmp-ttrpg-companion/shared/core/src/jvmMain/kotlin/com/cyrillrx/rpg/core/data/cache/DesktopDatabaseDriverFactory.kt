package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.core.data.cache.Database.Companion.DATABASE_NAME

class DesktopDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(URL_PREFIX + DATABASE_NAME)
        val targetVersion = AppDatabase.Schema.version

        when (val storedVersion = driver.schemaVersion()) {
            0L -> {
                AppDatabase.Schema.create(driver)
                driver.execute(null, "PRAGMA user_version = $targetVersion", 0)
            }
            in 1L until targetVersion -> {
                AppDatabase.Schema.migrate(driver, storedVersion, targetVersion)
                driver.execute(null, "PRAGMA user_version = $targetVersion", 0)
            }
        }

        return driver
    }

    companion object {
        const val URL_PREFIX = "jdbc:sqlite:"
    }
}

/**
 * Schema version of the database behind this driver, or `0` when it holds no application table.
 *
 * Falls back to probing the schema when `PRAGMA user_version` is unset, because a database written
 * before that bookkeeping existed reports `0` while already holding data. Trusting the pragma there
 * would run [AppDatabase.Schema.create], which no-ops on the existing `CREATE TABLE IF NOT EXISTS`
 * tables and then stamps the database as current — stranding the rows one migration short.
 */
internal fun SqlDriver.schemaVersion(): Long = readUserVersion().takeIf { it > 0L } ?: probeSchemaVersion()

private fun SqlDriver.readUserVersion(): Long = executeQuery(
    identifier = null,
    sql = "PRAGMA user_version",
    mapper = { cursor ->
        cursor.next()
        QueryResult.Value(cursor.getLong(0) ?: 0L)
    },
    parameters = 0,
).value

/**
 * Derives the version from the tables and columns each migration introduced. Only ever reached for
 * databases predating `PRAGMA user_version`, so it needs no case beyond the version that stamped it.
 */
private fun SqlDriver.probeSchemaVersion(): Long = when {
    hasTable("UserCollection") -> AppDatabase.Schema.version
    hasColumn("UserList", "updatedAt") -> 3L
    hasColumn("UserPreferencesEntity", "palette") -> 2L
    hasTable("UserPreferencesEntity") -> 1L
    else -> 0L
}

private fun SqlDriver.hasTable(table: String): Boolean = columnNamesOf(table).isNotEmpty()

private fun SqlDriver.hasColumn(table: String, column: String): Boolean = column in columnNamesOf(table)

private fun SqlDriver.columnNamesOf(table: String): Set<String> = executeQuery(
    identifier = null,
    sql = "PRAGMA table_info($table)",
    mapper = { cursor ->
        val columns = mutableSetOf<String>()
        while (cursor.next().value) {
            cursor.getString(1)?.let { columns += it }
        }
        QueryResult.Value(columns)
    },
    parameters = 0,
).value
