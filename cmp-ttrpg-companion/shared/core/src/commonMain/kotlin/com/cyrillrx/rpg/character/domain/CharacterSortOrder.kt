package com.cyrillrx.rpg.character.domain

import com.cyrillrx.core.domain.localizedSortKey
import com.cyrillrx.rpg.core.domain.Stored

enum class CharacterSortOrder { LAST_MODIFIED, NAME }

/** Sheets with no known date — the rows written before `updatedAt` existed — sink to the bottom. */
private val BY_LAST_MODIFIED: Comparator<Stored<Character>> =
    compareByDescending<Stored<Character>> { it.updatedAt }
        .thenBy { it.value.name.localizedSortKey() }
        .thenBy { it.value.id }

private val BY_NAME: Comparator<Stored<Character>> =
    compareBy<Stored<Character>> { it.value.name.localizedSortKey() }
        .thenByDescending { it.updatedAt }
        .thenBy { it.value.id }

/**
 * Total order, so two reads of the same sheets always render the same list: no repository promises
 * an order of its own, and ties bunch up on the sheets that carry no date at all.
 */
fun List<Stored<Character>>.applySort(order: CharacterSortOrder): List<Stored<Character>> = when (order) {
    CharacterSortOrder.LAST_MODIFIED -> sortedWith(BY_LAST_MODIFIED)
    CharacterSortOrder.NAME -> sortedWith(BY_NAME)
}
