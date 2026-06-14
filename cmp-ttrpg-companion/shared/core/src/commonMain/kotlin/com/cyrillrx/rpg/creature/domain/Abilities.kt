package com.cyrillrx.rpg.creature.domain

import kotlinx.serialization.Serializable

@Serializable
data class Abilities(
    val strength: AbilityScore,
    val dexterity: AbilityScore,
    val constitution: AbilityScore,
    val intelligence: AbilityScore,
    val wisdom: AbilityScore,
    val charisma: AbilityScore,
)
