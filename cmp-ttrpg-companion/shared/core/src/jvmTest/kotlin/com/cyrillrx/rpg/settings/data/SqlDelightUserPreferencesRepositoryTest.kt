package com.cyrillrx.rpg.settings.data

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import com.cyrillrx.rpg.core.domain.StoredSortOrder
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Palette
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.usercollection.domain.CollectionItemOrder
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    fun `every list order round-trips through the database`() = runTest {
        for (order in StoredSortOrder.entries) {
            // Opposite values on the two lists: a setter writing into the other column shows up here.
            val otherOrder = StoredSortOrder.entries.first { it != order }
            val repository = buildRepository()
            repository.initialize()

            repository.setCharacterSortOrder(order)
            repository.setCollectionSortOrder(otherOrder)
            repository.initialize() // re-reads from the database, proving the choices were persisted

            assertEquals(order, repository.preferences.value.characterSortOrder)
            assertEquals(otherOrder, repository.preferences.value.collectionSortOrder)
        }
        for (order in CollectionItemOrder.entries) {
            val repository = buildRepository()
            repository.initialize()

            repository.setCollectionItemOrder(order)
            repository.initialize()

            assertEquals(order, repository.preferences.value.collectionItemOrder)
        }
    }

    @Test
    fun `an order no longer known falls back to the default`() = runTest {
        val driver = TestDatabaseDriverFactory().createDriver()
        val repository = SqlDelightUserPreferencesRepository(
            object : DatabaseDriverFactory {
                override fun createDriver() = driver
            },
        )
        repository.initialize()
        driver.execute(
            null,
            "UPDATE UserPreferencesEntity SET character_sort_order = 'by_vibes', " +
                "collection_sort_order = 'by_vibes', collection_item_order = 'by_vibes' WHERE id = 1;",
            0,
        )

        repository.initialize() // re-reads what the database now holds

        val preferences = repository.preferences.value
        assertEquals(StoredSortOrder.LAST_MODIFIED, preferences.characterSortOrder)
        assertEquals(StoredSortOrder.LAST_MODIFIED, preferences.collectionSortOrder)
        assertEquals(CollectionItemOrder.ADDED, preferences.collectionItemOrder)
    }

    @Test
    fun `each list keeps its own order`() = runTest {
        val repository = buildRepository()
        repository.initialize()

        repository.setCharacterSortOrder(StoredSortOrder.NAME)
        repository.initialize() // re-reads: the in-memory state alone would hide a write to the wrong column

        val preferences = repository.preferences.value
        assertEquals(StoredSortOrder.NAME, preferences.characterSortOrder)
        assertEquals(StoredSortOrder.LAST_MODIFIED, preferences.collectionSortOrder)
        assertEquals(CollectionItemOrder.ADDED, preferences.collectionItemOrder)
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
    fun `reverting to the stored value while a write is in flight keeps the reverted value`() = runTest {
        // A dispatcher distinct from the test's own makes withContext suspend, so the second call runs mid-write.
        val repository = SqlDelightUserPreferencesRepository(
            TestDatabaseDriverFactory(),
            ioDispatcher = StandardTestDispatcher(testScheduler),
        )
        repository.initialize()

        launch { repository.setCollectionSortOrder(StoredSortOrder.NAME) }
        launch { repository.setCollectionSortOrder(StoredSortOrder.LAST_MODIFIED) }
        advanceUntilIdle()

        assertEquals(StoredSortOrder.LAST_MODIFIED, repository.preferences.value.collectionSortOrder)
        repository.initialize()
        assertEquals(StoredSortOrder.LAST_MODIFIED, repository.preferences.value.collectionSortOrder)
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
