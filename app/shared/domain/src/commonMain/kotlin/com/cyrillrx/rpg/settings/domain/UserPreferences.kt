package com.cyrillrx.rpg.settings.domain

import com.cyrillrx.rpg.core.domain.StoredSortOrder
import com.cyrillrx.rpg.usercollection.domain.CollectionItemOrder

data class UserPreferences(
    val theme: Theme = Theme.SYSTEM,
    val palette: Palette = Palette.ARCANE,
    val distanceUnit: DistanceUnit = DistanceUnit.FEET,
    val characterSortOrder: StoredSortOrder = StoredSortOrder.LAST_MODIFIED,
    val collectionSortOrder: StoredSortOrder = StoredSortOrder.LAST_MODIFIED,
    val collectionItemOrder: CollectionItemOrder = CollectionItemOrder.ADDED,
)
