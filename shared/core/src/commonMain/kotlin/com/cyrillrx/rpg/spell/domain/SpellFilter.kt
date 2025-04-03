package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter

class SpellFilter(
    private val query: String = "",
    private val schools: Set<Spell.School> = emptySet(),
    private val playerClasses: Set<PlayerCharacter.Class> = emptySet(),
    private val levels: Set<Int> = emptySet(),
) {
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
