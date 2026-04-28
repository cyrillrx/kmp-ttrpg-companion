package com.cyrillrx.rpg.magicalitem.domain

data class MagicalItemFilter(
    val query: String = "",
    val types: Set<MagicalItem.Type> = emptySet(),
    val rarities: Set<MagicalItem.Rarity> = emptySet(),
) {
    val hasActiveFilters: Boolean = types.isNotEmpty() || rarities.isNotEmpty()
}

fun List<MagicalItem>.applyFilter(filter: MagicalItemFilter?): List<MagicalItem> {
    if (filter == null) return this
    return filter { it.matches(filter) }
}

internal fun MagicalItem.matches(filter: MagicalItemFilter): Boolean {
    return (filter.types.isEmpty() || filter.types.contains(type)) &&
        (filter.rarities.isEmpty() || filter.rarities.contains(rarity)) &&
        (filter.query.isBlank() || matchesQuery(filter.query))
}

private fun MagicalItem.matchesQuery(query: String): Boolean {
    val trimmed = query.trim()
    return translations.values.any { translation ->
        translation.name.contains(trimmed, ignoreCase = true) ||
            translation.description.contains(trimmed, ignoreCase = true)
    }
}
