package com.cyrillrx.rpg.settings.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Palette
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SqlDelightUserPreferencesRepository(
    driverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserPreferencesRepository {
    private val db = Database(driverFactory)

    override val preferences: StateFlow<UserPreferences>
        field = MutableStateFlow(UserPreferences())

    override suspend fun initialize() {
        withContext(ioDispatcher) {
            db.initUserPreferences()
            preferences.value = db.getUserPreferences()
        }
    }

    override suspend fun setTheme(theme: Theme) {
        withContext(ioDispatcher) {
            db.updateTheme(theme)
            preferences.update { it.copy(theme = theme) }
        }
    }

    override suspend fun setPalette(palette: Palette) {
        withContext(ioDispatcher) {
            db.updatePalette(palette)
            preferences.update { it.copy(palette = palette) }
        }
    }

    override suspend fun setDistanceUnit(distanceUnit: DistanceUnit) {
        withContext(ioDispatcher) {
            db.updateDistanceUnit(distanceUnit)
            preferences.update { it.copy(distanceUnit = distanceUnit) }
        }
    }
}
