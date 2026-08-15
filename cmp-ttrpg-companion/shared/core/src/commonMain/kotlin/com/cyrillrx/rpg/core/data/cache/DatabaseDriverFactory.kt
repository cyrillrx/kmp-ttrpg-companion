package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.cache.AppDatabase

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver

    fun createDatabase(): AppDatabase = AppDatabase(createDriver())
}
