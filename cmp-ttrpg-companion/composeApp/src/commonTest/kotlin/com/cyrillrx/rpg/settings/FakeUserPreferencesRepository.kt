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

    var writeError: Exception? = null

    override val preferences: StateFlow<UserPreferences> = state

    override suspend fun initialize() = Unit

    override suspend fun setTheme(theme: Theme) {
        write { it.copy(theme = theme) }
    }

    override suspend fun setPalette(palette: Palette) {
        write { it.copy(palette = palette) }
    }

    override suspend fun setDistanceUnit(unit: DistanceUnit) {
        write { it.copy(distanceUnit = unit) }
    }

    override suspend fun setCharacterSortOrder(order: StoredSortOrder) {
        write { it.copy(characterSortOrder = order) }
    }

    override suspend fun setCollectionSortOrder(order: StoredSortOrder) {
        write { it.copy(collectionSortOrder = order) }
    }

    override suspend fun setCollectionItemOrder(order: CollectionItemOrder) {
        write { it.copy(collectionItemOrder = order) }
    }

    private fun write(update: (UserPreferences) -> UserPreferences) {
        writeError?.let { throw it }
        state.update(update)
    }
}
