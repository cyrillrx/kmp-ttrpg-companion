package com.cyrillrx.rpg.settings.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SqlDelightUserPreferencesRepository(driverFactory: DatabaseDriverFactory) : UserPreferencesRepository {
    private val db = Database(driverFactory).also { it.initUserPreferences() }
    private val _preferences = MutableStateFlow(db.getUserPreferences())

    override val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    override suspend fun setTheme(theme: Theme) {
        db.updateTheme(theme)
        _preferences.update { it.copy(theme = theme) }
    }

    override suspend fun setDistanceUnit(distanceUnit: DistanceUnit) {
        db.updateDistanceUnit(distanceUnit)
        _preferences.update { it.copy(distanceUnit = distanceUnit) }
    }
}
