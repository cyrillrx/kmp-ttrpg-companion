package com.cyrillrx.rpg.creature.domain

data class CreatureFilter(
    val query: String = "",
    val types: Set<Monster.Type> = emptySet(),
    val challengeRatings: Set<Float> = emptySet(),
) {
    val hasActiveFilters: Boolean = types.isNotEmpty() || challengeRatings.isNotEmpty()
}

fun List<Monster>.applyFilter(filter: CreatureFilter?): List<Monster> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

internal fun Monster.matches(filter: CreatureFilter): Boolean {
    return (filter.types.isEmpty() || filter.types.contains(type)) &&
        (filter.challengeRatings.isEmpty() || filter.challengeRatings.contains(challengeRating)) &&
        (filter.query.isBlank() || matches(filter.query))
}

private fun Monster.matches(query: String): Boolean {
    val trimmedQuery = query.trim()
    return translations.values.any { t ->
        t.name.contains(trimmedQuery, ignoreCase = true) ||
            t.description.contains(trimmedQuery, ignoreCase = true)
    } || name.contains(trimmedQuery, ignoreCase = true)
}
