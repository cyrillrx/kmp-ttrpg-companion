package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.Character

enum class ComponentFilterState {
    NONE,
    REQUIRED,
    EXCLUDED,
    ;

    fun next(): ComponentFilterState = when (this) {
        NONE -> REQUIRED
        REQUIRED -> EXCLUDED
        EXCLUDED -> NONE
    }
}

/** Cycles [component] to its next state, dropping the entry when it returns to [ComponentFilterState.NONE]. */
fun Map<Spell.ComponentType, ComponentFilterState>.cycled(
    component: Spell.ComponentType,
): Map<Spell.ComponentType, ComponentFilterState> {
    val nextState = (this[component] ?: ComponentFilterState.NONE).next()
    return if (nextState == ComponentFilterState.NONE) this - component else this + (component to nextState)
}

data class SpellFilter(
    val query: String = "",
    val schools: Set<Spell.School> = emptySet(),
    val characterClasses: Set<Character.Class> = emptySet(),
    val levels: Set<Int> = emptySet(),
    val components: Map<Spell.ComponentType, ComponentFilterState> = emptyMap(),
) {
    val hasActiveFilters: Boolean =
        schools.isNotEmpty() ||
            characterClasses.isNotEmpty() ||
            levels.isNotEmpty() ||
            components.values.any { it != ComponentFilterState.NONE }

    internal val trimmedQuery: String = query.trim()
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
                ComponentFilterState.NONE -> true
                ComponentFilterState.REQUIRED -> component.isPresentIn(components)
                ComponentFilterState.EXCLUDED -> !component.isPresentIn(components)
            }
        } &&
        (filter.trimmedQuery.isEmpty() || matchesQuery(filter.trimmedQuery))

private fun Spell.ComponentType.isPresentIn(components: Spell.Components): Boolean = when (this) {
    Spell.ComponentType.VERBAL -> components.verbal
    Spell.ComponentType.SOMATIC -> components.somatic
    Spell.ComponentType.MATERIAL -> components.material
}

private fun Spell.matchesQuery(query: String): Boolean =
    translations.values.any { translation ->
        translation.name.contains(query, ignoreCase = true) ||
            translation.description.contains(query, ignoreCase = true)
    }
