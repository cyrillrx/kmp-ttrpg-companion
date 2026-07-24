package com.cyrillrx.rpg.settings.data

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Palette
import com.cyrillrx.rpg.settings.domain.Theme
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SqlDelightUserPreferencesRepositoryTest {

    private fun buildRepository() = SqlDelightUserPreferencesRepository(TestDatabaseDriverFactory())

    @Test
    fun `initialize exposes the default palette`() = runTest {
        val repository = buildRepository()
        repository.initialize()

        assertEquals(Palette.ARCANE, repository.preferences.value.palette)
    }

    @Test
    fun `setPalette updates the exposed preferences`() = runTest {
        val repository = buildRepository()
        repository.initialize()

        repository.setPalette(Palette.DRAGON)

        assertEquals(Palette.DRAGON, repository.preferences.value.palette)
    }

    @Test
    fun `every palette round-trips through the database`() = runTest {
        for (palette in Palette.entries) {
            val repository = buildRepository()
            repository.initialize()

            repository.setPalette(palette)
            repository.initialize() // re-reads from the database, proving the choice was persisted

            assertEquals(palette, repository.preferences.value.palette)
        }
    }

    @Test
    fun `setPalette skips the database write when the value is unchanged`() = runTest {
        val driver = WriteCountingDriver(TestDatabaseDriverFactory().createDriver())
        val repository = SqlDelightUserPreferencesRepository(
            object : DatabaseDriverFactory {
                override fun createDriver() = driver
            },
        )
        repository.initialize()

        val writesBefore = driver.writeCount
        repository.setPalette(Palette.ARCANE) // already the default: must not touch the database
        assertEquals(writesBefore, driver.writeCount)

        repository.setPalette(Palette.DRAGON) // a real change: a write happens
        assertTrue(driver.writeCount > writesBefore)
    }

    @Test
    fun `setPalette leaves theme and distance unit untouched`() = runTest {
        val repository = buildRepository()
        repository.initialize()
        repository.setTheme(Theme.DARK)
        repository.setDistanceUnit(DistanceUnit.METERS)

        repository.setPalette(Palette.DRAGON)

        val preferences = repository.preferences.value
        assertEquals(Palette.DRAGON, preferences.palette)
        assertEquals(Theme.DARK, preferences.theme)
        assertEquals(DistanceUnit.METERS, preferences.distanceUnit)
    }
}

private class WriteCountingDriver(private val delegate: SqlDriver) : SqlDriver by delegate {
    var writeCount = 0
        private set

    override fun execute(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?,
    ): QueryResult<Long> {
        writeCount++
        return delegate.execute(identifier, sql, parameters, binders)
    }
}
