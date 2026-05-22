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
        val storedVersion = driver.executeQuery(
            identifier = null,
            sql = "PRAGMA user_version",
            mapper = { cursor -> QueryResult.Value(cursor.getLong(0) ?: 0L) },
            parameters = 0,
        ).value

        when {
            storedVersion == 0L -> {
                AppDatabase.Schema.create(driver)
                driver.execute(null, "PRAGMA user_version = $targetVersion", 0)
            }
            storedVersion < targetVersion -> {
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
