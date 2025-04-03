package com.cyrillrx.rpg.magicalitem.domain

class MagicalItemFilter(
    private val query: String = "",
    private val types: Set<MagicalItem.Type> = emptySet(),
    private val rarities: Set<MagicalItem.Rarity> = emptySet(),
) {
    fun matches(item: MagicalItem): Boolean {
        return (types.isEmpty() || types.contains(item.type)) &&
            (rarities.isEmpty() || rarities.contains(item.rarity)) &&
            (query.isBlank() || item.matches(query))
    }

    private fun MagicalItem.matches(query: String): Boolean {
        val trimmedQuery = query.trim()

        return title.contains(trimmedQuery, ignoreCase = true) ||
            subtitle.contains(trimmedQuery, ignoreCase = true) ||
            description.contains(trimmedQuery, ignoreCase = true)
    }
}
