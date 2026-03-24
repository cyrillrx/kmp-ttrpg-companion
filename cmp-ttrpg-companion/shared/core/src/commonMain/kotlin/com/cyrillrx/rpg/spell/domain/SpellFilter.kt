package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter

data class SpellFilter(
    val query: String = "",
    val schools: Set<Spell.School> = emptySet(),
    val playerClasses: Set<PlayerCharacter.Class> = emptySet(),
    val levels: Set<Int> = emptySet(),
) {
    val hasActiveFilters: Boolean = schools.isNotEmpty() || playerClasses.isNotEmpty() || levels.isNotEmpty()

    fun matches(spell: Spell): Boolean {
        return (schools.isEmpty() || spell.schools.any { schools.contains(it) }) &&
            (playerClasses.isEmpty() || spell.availableClasses.any { playerClasses.contains(it) }) &&
            (levels.isEmpty() || levels.contains(spell.level)) &&
            (query.isBlank() || spell.matches(query))
    }

    private fun Spell.matches(query: String): Boolean {
        val trimmedQuery = query.trim()

        return title.contains(trimmedQuery, ignoreCase = true) ||
            description.contains(trimmedQuery, ignoreCase = true)
    }
}
