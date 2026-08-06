package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.cache.AppDatabase
import kotlin.concurrent.Volatile

/**
 * Wraps a [DatabaseDriverFactory] so the underlying [SqlDriver] is created once and shared by every
 * repository, instead of each repository opening its own connection to the same database file.
 *
 * Sharing holds per instance: a second instance opens a second connection. Platform entry points
 * therefore build exactly one instance and keep it out of composition, so that a recomposition
 * cannot silently open another connection.
 */
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

    /**
     * Releases the shared driver, without opening one when no repository ever asked for it.
     *
     * Terminal: the factory hands nothing out afterwards. Returning the released driver instead
     * would surface as a driver-level failure on whichever query happened to run next, far from the
     * call that closed it.
     */
    fun close() {
        closed = true
        if (lazyDriver.isInitialized()) lazyDriver.value.close()
    }

    private fun checkNotClosed() = check(!closed) { "This factory is closed: its driver was released." }
}
