package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.core.data.cache.Database.Companion.DATABASE_NAME

class DesktopDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(URL_PREFIX + DATABASE_NAME)

        try {
            AppDatabase.Schema.create(driver)
        } catch (e: Exception) {
            // DB already exists 🤷‍♂️
        }
        return driver
    }

    companion object {
        const val URL_PREFIX = "jdbc:sqlite:"
    }
}
