package com.cyrillrx.rpg.character.domain

class PlayerCharacterFilter(
    val query: String = "",
)

fun List<PlayerCharacter>.applyFilter(filter: PlayerCharacterFilter?): List<PlayerCharacter> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

fun PlayerCharacter.matches(filter: PlayerCharacterFilter): Boolean {
    val trimmedQuery = filter.query.trim()

    return name.contains(trimmedQuery, ignoreCase = true)
}
