package com.cyrillrx.rpg.character.domain

class PlayerCharacterFilter(
    private val query: String = "",
) {
    fun matches(playerCharacter: PlayerCharacter): Boolean {
        val trimmedQuery = query.trim()

        return playerCharacter.name.contains(trimmedQuery, ignoreCase = true)
    }
}
