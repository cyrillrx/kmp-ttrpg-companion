package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import java.util.concurrent.atomic.AtomicInteger

/** Counts how many drivers were opened, so a test can assert when a database is actually touched. */
class CountingDriverFactory : DatabaseDriverFactory {
    // Atomic so that a memoization regression shows up as a real count under concurrent calls,
    // rather than as lost increments that happen to match the expected one.
    private val count = AtomicInteger()

    val createCount: Int get() = count.get()

    override fun createDriver(): SqlDriver {
        count.incrementAndGet()
        return TestDatabaseDriverFactory().createDriver()
    }
}
