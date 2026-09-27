package com.cyrillrx.rpg.settings.domain

import com.cyrillrx.rpg.core.domain.StoredSortOrder
import com.cyrillrx.rpg.usercollection.domain.CollectionItemOrder
import kotlinx.coroutines.flow.StateFlow

interface UserPreferencesRepository {
    val preferences: StateFlow<UserPreferences>
    suspend fun initialize()
    suspend fun setTheme(theme: Theme)
    suspend fun setPalette(palette: Palette)
    suspend fun setDistanceUnit(unit: DistanceUnit)
    suspend fun setCharacterSortOrder(order: StoredSortOrder)
    suspend fun setCollectionSortOrder(order: StoredSortOrder)
    suspend fun setCollectionItemOrder(order: CollectionItemOrder)
}
