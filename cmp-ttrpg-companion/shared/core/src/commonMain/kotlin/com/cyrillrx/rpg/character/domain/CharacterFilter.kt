package com.cyrillrx.rpg.character.domain

class CharacterFilter(
    val query: String = "",
)

fun List<Character>.applyFilter(filter: CharacterFilter?): List<Character> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

internal fun Character.matches(filter: CharacterFilter): Boolean {
    val trimmedQuery = filter.query.trim()

    return name.contains(trimmedQuery, ignoreCase = true)
}
