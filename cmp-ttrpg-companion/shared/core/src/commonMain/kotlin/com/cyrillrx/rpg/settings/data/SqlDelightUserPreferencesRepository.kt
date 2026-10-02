package com.cyrillrx.rpg.settings.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.domain.StoredSortOrder
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Palette
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import com.cyrillrx.rpg.usercollection.domain.CollectionItemOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class SqlDelightUserPreferencesRepository(
    driverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserPreferencesRepository {
    private val db = Database(driverFactory)

    override val preferences: StateFlow<UserPreferences>
        field = MutableStateFlow(UserPreferences())

    private val writeLock = Mutex()

    override suspend fun initialize() {
        writeLock.withLock {
            withContext(ioDispatcher) {
                db.initUserPreferences()
                preferences.value = db.getUserPreferences()
            }
        }
    }

    override suspend fun setTheme(theme: Theme) {
        writeLock.withLock {
            if (preferences.value.theme == theme) return
            withContext(ioDispatcher) {
                db.updateTheme(theme)
                preferences.update { it.copy(theme = theme) }
            }
        }
    }

    override suspend fun setPalette(palette: Palette) {
        writeLock.withLock {
            if (preferences.value.palette == palette) return
            withContext(ioDispatcher) {
                db.updatePalette(palette)
                preferences.update { it.copy(palette = palette) }
            }
        }
    }

    override suspend fun setCharacterSortOrder(order: StoredSortOrder) {
        writeLock.withLock {
            if (preferences.value.characterSortOrder == order) return
            withContext(ioDispatcher) {
                db.updateCharacterSortOrder(order)
                preferences.update { it.copy(characterSortOrder = order) }
            }
        }
    }

    override suspend fun setCollectionSortOrder(order: StoredSortOrder) {
        writeLock.withLock {
            if (preferences.value.collectionSortOrder == order) return
            withContext(ioDispatcher) {
                db.updateCollectionSortOrder(order)
                preferences.update { it.copy(collectionSortOrder = order) }
            }
        }
    }

    override suspend fun setCollectionItemOrder(order: CollectionItemOrder) {
        writeLock.withLock {
            if (preferences.value.collectionItemOrder == order) return
            withContext(ioDispatcher) {
                db.updateCollectionItemOrder(order)
                preferences.update { it.copy(collectionItemOrder = order) }
            }
        }
    }

    override suspend fun setDistanceUnit(distanceUnit: DistanceUnit) {
        writeLock.withLock {
            if (preferences.value.distanceUnit == distanceUnit) return
            withContext(ioDispatcher) {
                db.updateDistanceUnit(distanceUnit)
                preferences.update { it.copy(distanceUnit = distanceUnit) }
            }
        }
    }
}
