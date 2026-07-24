package com.cyrillrx.rpg.settings.data

import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Palette
import com.cyrillrx.rpg.settings.domain.Theme
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
