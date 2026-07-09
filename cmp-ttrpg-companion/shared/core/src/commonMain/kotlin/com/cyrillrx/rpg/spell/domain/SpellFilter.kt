package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.Character

enum class ComponentFilter { REQUIRED, EXCLUDED }

data class SpellFilter(
    val query: String = "",
    val schools: Set<Spell.School> = emptySet(),
    val characterClasses: Set<Character.Class> = emptySet(),
    val levels: Set<Int> = emptySet(),
    val components: Map<Spell.ComponentType, ComponentFilter> = emptyMap(),
) {
    val hasActiveFilters: Boolean =
        schools.isNotEmpty() || characterClasses.isNotEmpty() || levels.isNotEmpty() || components.isNotEmpty()
}

fun List<Spell>.applyFilter(filter: SpellFilter?): List<Spell> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

internal fun Spell.matches(filter: SpellFilter): Boolean =
    (filter.schools.isEmpty() || school in filter.schools) &&
        (filter.characterClasses.isEmpty() || availableClasses.any { filter.characterClasses.contains(it) }) &&
        (filter.levels.isEmpty() || filter.levels.contains(level)) &&
        filter.components.all { (component, state) ->
            when (state) {
                ComponentFilter.REQUIRED -> component.isPresentIn(components)
                ComponentFilter.EXCLUDED -> !component.isPresentIn(components)
            }
        } &&
        (filter.query.isBlank() || matchesQuery(filter.query))

private fun Spell.ComponentType.isPresentIn(components: Spell.Components): Boolean = when (this) {
    Spell.ComponentType.VERBAL -> components.verbal
    Spell.ComponentType.SOMATIC -> components.somatic
    Spell.ComponentType.MATERIAL -> components.material
}

private fun Spell.matchesQuery(query: String): Boolean {
    val trimmed = query.trim()
    return translations.values.any { translation ->
        translation.name.contains(trimmed, ignoreCase = true) ||
            translation.description.contains(trimmed, ignoreCase = true)
    }
}
