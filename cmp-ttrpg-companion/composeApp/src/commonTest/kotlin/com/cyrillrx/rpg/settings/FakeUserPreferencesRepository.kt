package com.cyrillrx.rpg.settings

import com.cyrillrx.rpg.core.domain.StoredSortOrder
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Palette
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import com.cyrillrx.rpg.usercollection.domain.CollectionItemOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesRepository(initial: UserPreferences = UserPreferences()) : UserPreferencesRepository {

    private val state = MutableStateFlow(initial)

    override val preferences: StateFlow<UserPreferences> = state

    override suspend fun initialize() = Unit

    override suspend fun setTheme(theme: Theme) {
        state.update { it.copy(theme = theme) }
    }

    override suspend fun setPalette(palette: Palette) {
        state.update { it.copy(palette = palette) }
    }

    override suspend fun setDistanceUnit(unit: DistanceUnit) {
        state.update { it.copy(distanceUnit = unit) }
    }

    override suspend fun setCharacterSortOrder(order: StoredSortOrder) {
        state.update { it.copy(characterSortOrder = order) }
    }

    override suspend fun setCollectionSortOrder(order: StoredSortOrder) {
        state.update { it.copy(collectionSortOrder = order) }
    }

    override suspend fun setCollectionItemOrder(order: CollectionItemOrder) {
        state.update { it.copy(collectionItemOrder = order) }
    }
}
