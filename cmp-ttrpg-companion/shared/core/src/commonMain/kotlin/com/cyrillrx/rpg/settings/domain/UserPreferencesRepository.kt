package com.cyrillrx.rpg.settings.domain

import kotlinx.coroutines.flow.StateFlow

interface UserPreferencesRepository {
    val preferences: StateFlow<UserPreferences>
    suspend fun initialize()
    suspend fun setTheme(theme: Theme)
    suspend fun setDistanceUnit(unit: DistanceUnit)
}
