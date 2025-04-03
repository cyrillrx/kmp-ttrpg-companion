package com.cyrillrx.rpg.creature.domain

class CreatureFilter(
    private val query: String = "",
    private val types: Set<Creature.Type> = emptySet(),
    private val challengeRatings: Set<Float> = emptySet(),
) {
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
