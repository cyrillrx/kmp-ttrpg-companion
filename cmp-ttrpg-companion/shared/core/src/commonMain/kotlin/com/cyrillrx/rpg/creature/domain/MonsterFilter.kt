package com.cyrillrx.rpg.creature.domain

data class MonsterFilter(
    val query: String = "",
    val types: Set<Monster.Type> = emptySet(),
    val challengeRatings: Set<Float> = emptySet(),
) {
    val hasActiveFilters: Boolean = types.isNotEmpty() || challengeRatings.isNotEmpty()

    internal val trimmedQuery: String = query.trim()
}

fun List<Monster>.applyFilter(filter: MonsterFilter?): List<Monster> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

internal fun Monster.matches(filter: MonsterFilter): Boolean =
    (filter.types.isEmpty() || filter.types.any { it in types }) &&
        (filter.challengeRatings.isEmpty() || filter.challengeRatings.contains(challengeRating)) &&
        (filter.trimmedQuery.isEmpty() || matches(filter.trimmedQuery))

private fun Monster.matches(query: String): Boolean =
    translations.values.any { t ->
        t.name.contains(query, ignoreCase = true) ||
            t.description.contains(query, ignoreCase = true)
    }
