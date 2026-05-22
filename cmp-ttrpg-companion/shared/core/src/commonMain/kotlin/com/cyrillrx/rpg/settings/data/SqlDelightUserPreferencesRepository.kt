package com.cyrillrx.rpg.settings.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SqlDelightUserPreferencesRepository(driverFactory: DatabaseDriverFactory) : UserPreferencesRepository {
    private val db = Database(driverFactory)
    private val _preferences = MutableStateFlow(UserPreferences())

    override val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            db.initUserPreferences()
            _preferences.value = db.getUserPreferences()
        }
    }

    override suspend fun setTheme(theme: Theme) {
        withContext(Dispatchers.IO) { db.updateTheme(theme) }
        _preferences.update { it.copy(theme = theme) }
    }

    override suspend fun setDistanceUnit(distanceUnit: DistanceUnit) {
        withContext(Dispatchers.IO) { db.updateDistanceUnit(distanceUnit) }
        _preferences.update { it.copy(distanceUnit = distanceUnit) }
    }
}
