package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.Character

data class SpellFilter(
    val query: String = "",
    val schools: Set<Spell.School> = emptySet(),
    val characterClasses: Set<Character.Class> = emptySet(),
    val levels: Set<Int> = emptySet(),
) {
    val hasActiveFilters: Boolean = schools.isNotEmpty() || characterClasses.isNotEmpty() || levels.isNotEmpty()
}

fun List<Spell>.applyFilter(filter: SpellFilter?): List<Spell> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

internal fun Spell.matches(filter: SpellFilter): Boolean =
    (filter.schools.isEmpty() || school in filter.schools) &&
        (filter.characterClasses.isEmpty() || availableClasses.any { filter.characterClasses.contains(it) }) &&
        (filter.levels.isEmpty() || filter.levels.contains(level)) &&
        (filter.query.isBlank() || matchesQuery(filter.query))

private fun Spell.matchesQuery(query: String): Boolean {
    val trimmed = query.trim()
    return translations.values.any { translation ->
        translation.name.contains(trimmed, ignoreCase = true) ||
            translation.description.contains(trimmed, ignoreCase = true)
    }
}
