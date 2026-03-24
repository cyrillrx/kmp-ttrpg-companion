package com.cyrillrx.rpg.creature.domain

data class CreatureFilter(
    val query: String = "",
    val types: Set<Creature.Type> = emptySet(),
    val challengeRatings: Set<Float> = emptySet(),
) {
    val hasActiveFilters: Boolean = types.isNotEmpty() || challengeRatings.isNotEmpty()

    fun matches(creature: Creature): Boolean {
        return (types.isEmpty() || types.contains(creature.type)) &&
            (challengeRatings.isEmpty() || challengeRatings.contains(creature.challengeRating)) &&
            (query.isBlank() || creature.matches(query))
    }

    private fun BaseCreature.matches(query: String): Boolean {
        val trimmedQuery = query.trim()

        return name.lowercase().contains(trimmedQuery) ||
            description.lowercase().contains(trimmedQuery)
    }
}
