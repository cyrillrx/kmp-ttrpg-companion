package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.cache.AppDatabase
import kotlin.concurrent.Volatile

class SharedDatabaseDriverFactory(
    private val delegate: DatabaseDriverFactory,
) : DatabaseDriverFactory {
    private val lazyDriver = lazy { delegate.createDriver() }
    private val lazyDatabase = lazy { AppDatabase(lazyDriver.value) }

    @Volatile
    private var closed = false

    override fun createDriver(): SqlDriver {
        checkNotClosed()
        return lazyDriver.value
    }

    override fun createDatabase(): AppDatabase {
        checkNotClosed()
        return lazyDatabase.value
    }

    fun close() {
        closed = true
        if (lazyDriver.isInitialized()) lazyDriver.value.close()
    }

    private fun checkNotClosed() = check(!closed) { "This factory is closed: its driver was released." }
}
