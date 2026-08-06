package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.cache.AppDatabase

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver

    /**
     * Overridden by [SharedDatabaseDriverFactory] to hand out one instance. Several handles over a
     * single driver do work — SQLDelight scopes transactions and query listeners to the driver —
     * but that is a runtime detail, and the wiring should not depend on it.
     */
    fun createDatabase(): AppDatabase = AppDatabase(createDriver())
}
