package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.core.domain.Stored
import kotlin.jvm.JvmName

class CharacterFilter(
    val query: String = "",
)

fun List<Character>.applyFilter(filter: CharacterFilter?): List<Character> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

@JvmName("applyFilterToStored")
fun List<Stored<Character>>.applyFilter(filter: CharacterFilter?): List<Stored<Character>> {
    if (filter == null) return this
    return filter { it.value.matches(filter) }
}

internal fun Character.matches(filter: CharacterFilter): Boolean {
    val trimmedQuery = filter.query.trim()

    return name.contains(trimmedQuery, ignoreCase = true)
}
