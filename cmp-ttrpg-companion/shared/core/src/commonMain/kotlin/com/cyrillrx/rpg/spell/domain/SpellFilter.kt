package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter

data class SpellFilter(
    val query: String = "",
    val schools: Set<Spell.School> = emptySet(),
    val playerClasses: Set<PlayerCharacter.Class> = emptySet(),
    val levels: Set<Int> = emptySet(),
) {
    val hasActiveFilters: Boolean = schools.isNotEmpty() || playerClasses.isNotEmpty() || levels.isNotEmpty()
}

fun Spell.matches(filter: SpellFilter): Boolean {
    return (filter.schools.isEmpty() || schools.any { filter.schools.contains(it) }) &&
        (filter.playerClasses.isEmpty() || availableClasses.any { filter.playerClasses.contains(it) }) &&
        (filter.levels.isEmpty() || filter.levels.contains(level)) &&
        (filter.query.isBlank() || matches(filter.query))
}

private fun Spell.matches(query: String): Boolean {
    val trimmedQuery = query.trim()

    return title.contains(trimmedQuery, ignoreCase = true) ||
        description.contains(trimmedQuery, ignoreCase = true)
}
