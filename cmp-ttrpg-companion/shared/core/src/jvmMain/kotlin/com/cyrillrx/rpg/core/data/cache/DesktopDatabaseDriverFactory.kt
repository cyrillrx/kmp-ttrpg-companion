package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.core.data.cache.Database.Companion.DATABASE_NAME

class DesktopDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver =
        JdbcSqliteDriver(URL_PREFIX + DATABASE_NAME).also { it.reconcileSchema() }

    companion object {
        const val URL_PREFIX = "jdbc:sqlite:"
    }
}

/** Brings the database behind this driver up to [AppDatabase.Schema]'s version, stamping it as it goes. */
internal fun SqlDriver.reconcileSchema() {
    val targetVersion = AppDatabase.Schema.version
    val storedVersion = schemaVersion()

    // A database at or beyond the target needs nothing: one written by a more recent build
    // keeps its own stamp rather than being pulled back to this build's version.
    if (storedVersion >= targetVersion) return

    // Schema and version stamp commit together: SQLite rolls DDL back with the transaction,
    // so an interrupted launch retries from the version it started at rather than resuming
    // halfway through a migration the database already claims to have applied.
    transactional {
        if (storedVersion == 0L) {
            AppDatabase.Schema.create(this)
        } else {
            AppDatabase.Schema.migrate(this, storedVersion, targetVersion)
        }
        execute(null, "PRAGMA user_version = $targetVersion", 0)
    }
}

private fun SqlDriver.transactional(block: () -> Unit) {
    execute(null, "BEGIN", 0)
    try {
        block()
        execute(null, "COMMIT", 0)
    } catch (throwable: Throwable) {
        execute(null, "ROLLBACK", 0)
        throw throwable
    }
}

/** Schema version the database behind this driver was last stamped with, or `0` when it never was. */
internal fun SqlDriver.schemaVersion(): Long = executeQuery(
    identifier = null,
    sql = "PRAGMA user_version",
    mapper = { cursor ->
        cursor.next()
        QueryResult.Value(cursor.getLong(0) ?: 0L)
    },
    parameters = 0,
).value
