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

fun List<Spell>.applyFilter(filter: SpellFilter?): List<Spell> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

internal fun Spell.matches(filter: SpellFilter): Boolean {
    return (filter.schools.isEmpty() || school in filter.schools) &&
        (filter.playerClasses.isEmpty() || availableClasses.any { filter.playerClasses.contains(it) }) &&
        (filter.levels.isEmpty() || filter.levels.contains(level)) &&
        (filter.query.isBlank() || matchesQuery(filter.query))
}

private fun Spell.matchesQuery(query: String): Boolean {
    val trimmed = query.trim()
    return translations.values.any { translation ->
        translation.name.contains(trimmed, ignoreCase = true) ||
            translation.description.contains(trimmed, ignoreCase = true)
    }
}
