package com.cyrillrx.rpg.magicalitem.domain

data class MagicalItemFilter(
    val query: String = "",
    val types: Set<MagicalItem.Type> = emptySet(),
    val rarities: Set<MagicalItem.Rarity> = emptySet(),
) {
    val hasActiveFilters: Boolean = types.isNotEmpty() || rarities.isNotEmpty()

    internal val trimmedQuery: String = query.trim()
}

fun List<MagicalItem>.applyFilter(filter: MagicalItemFilter?): List<MagicalItem> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

internal fun MagicalItem.matches(filter: MagicalItemFilter): Boolean =
    (filter.types.isEmpty() || filter.types.contains(type)) &&
        (filter.rarities.isEmpty() || filter.rarities.contains(rarity)) &&
        (filter.trimmedQuery.isEmpty() || matchesQuery(filter.trimmedQuery))

private fun MagicalItem.matchesQuery(query: String): Boolean =
    translations.values.any { translation ->
        translation.name.contains(query, ignoreCase = true) ||
            translation.description.contains(query, ignoreCase = true)
    }
