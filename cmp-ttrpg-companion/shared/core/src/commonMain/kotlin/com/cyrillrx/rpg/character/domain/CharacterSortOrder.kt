package com.cyrillrx.rpg.character.domain

import com.cyrillrx.core.domain.localizedSortKey
import com.cyrillrx.rpg.core.domain.Stored

enum class CharacterSortOrder { LAST_MODIFIED, NAME }

private val BY_LAST_MODIFIED: Comparator<Stored<Character>> =
    compareByDescending<Stored<Character>> { it.updatedAt }
        .thenBy { it.value.id }

private val BY_NAME: Comparator<Stored<Character>> =
    compareBy<Stored<Character>> { it.value.name.localizedSortKey() }
        .thenBy { it.value.id }

fun List<Stored<Character>>.applySort(order: CharacterSortOrder): List<Stored<Character>> = when (order) {
    CharacterSortOrder.LAST_MODIFIED -> sortedWith(BY_LAST_MODIFIED)
    CharacterSortOrder.NAME -> sortedWith(BY_NAME)
}
