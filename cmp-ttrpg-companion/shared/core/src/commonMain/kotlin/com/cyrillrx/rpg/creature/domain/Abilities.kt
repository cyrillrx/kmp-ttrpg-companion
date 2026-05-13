package com.cyrillrx.rpg.creature.domain

import kotlinx.serialization.Serializable

@Serializable
class Abilities(
    val str: Ability,
    val dex: Ability,
    val con: Ability,
    val int: Ability,
    val wis: Ability,
    val cha: Ability,
)
