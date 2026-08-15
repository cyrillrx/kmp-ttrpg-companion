package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import java.util.concurrent.atomic.AtomicInteger

class CountingDriverFactory : DatabaseDriverFactory {
    private val count = AtomicInteger()

    val createCount: Int get() = count.get()

    override fun createDriver(): SqlDriver {
        count.incrementAndGet()
        return TestDatabaseDriverFactory().createDriver()
    }
}
