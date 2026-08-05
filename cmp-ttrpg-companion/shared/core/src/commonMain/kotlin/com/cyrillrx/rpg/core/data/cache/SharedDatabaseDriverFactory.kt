package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver

/**
 * Wraps a [DatabaseDriverFactory] so the underlying [SqlDriver] is created once and shared by every
 * repository, instead of each repository opening its own connection to the same database file.
 */
class SharedDatabaseDriverFactory(
    private val delegate: DatabaseDriverFactory,
) : DatabaseDriverFactory {
    private val driver: SqlDriver by lazy { delegate.createDriver() }

    override fun createDriver(): SqlDriver = driver
}
