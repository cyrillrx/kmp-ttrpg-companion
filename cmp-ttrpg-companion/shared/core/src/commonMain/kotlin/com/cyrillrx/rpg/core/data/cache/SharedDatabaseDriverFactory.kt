package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver

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

    override fun createDriver(): SqlDriver = lazyDriver.value

    /** Releases the shared driver, without opening one when no repository ever asked for it. */
    fun close() {
        if (lazyDriver.isInitialized()) lazyDriver.value.close()
    }
}
