package com.cyrillrx.rpg.creature.domain

import kotlinx.serialization.Serializable

@Serializable
data class Abilities(
    val strength: Ability,
    val dexterity: Ability,
    val constitution: Ability,
    val intelligence: Ability,
    val wisdom: Ability,
    val charisma: Ability,
)
