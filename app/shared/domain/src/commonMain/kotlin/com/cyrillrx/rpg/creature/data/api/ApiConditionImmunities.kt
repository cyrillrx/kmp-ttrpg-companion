package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiConditionImmunities(
    val blinded: Boolean?,
    val charmed: Boolean?,
    val deafened: Boolean?,
    val exhaustion: Boolean?,
    val frightened: Boolean?,
    val grappled: Boolean?,
    val incapacitated: Boolean?,
    val paralyzed: Boolean?,
    val petrified: Boolean?,
    val poisoned: Boolean?,
    val prone: Boolean?,
    val restrained: Boolean?,
    val stunned: Boolean?,
    val unconscious: Boolean?,
)
