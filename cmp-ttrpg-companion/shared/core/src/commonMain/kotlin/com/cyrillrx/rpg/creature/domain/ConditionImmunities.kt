package com.cyrillrx.rpg.creature.domain

data class ConditionImmunities(
    val blinded: Boolean = false,
    val charmed: Boolean = false,
    val deafened: Boolean = false,
    val exhaustion: Boolean = false,
    val frightened: Boolean = false,
    val grappled: Boolean = false,
    val incapacitated: Boolean = false,
    val paralyzed: Boolean = false,
    val petrified: Boolean = false,
    val poisoned: Boolean = false,
    val prone: Boolean = false,
    val restrained: Boolean = false,
    val stunned: Boolean = false,
    val unconscious: Boolean = false,
)
