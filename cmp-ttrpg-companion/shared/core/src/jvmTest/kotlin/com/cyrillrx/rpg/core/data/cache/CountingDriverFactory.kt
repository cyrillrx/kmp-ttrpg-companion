package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver

/** Counts how many drivers were opened, so a test can assert when a database is actually touched. */
class CountingDriverFactory : DatabaseDriverFactory {
    var createCount = 0
        private set

    override fun createDriver(): SqlDriver {
        createCount++
        return TestDatabaseDriverFactory().createDriver()
    }
}
