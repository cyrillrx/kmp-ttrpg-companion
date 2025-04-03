package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.core.data.cache.Database.Companion.DATABASE_NAME

class IOSDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, DATABASE_NAME)
    }
}
