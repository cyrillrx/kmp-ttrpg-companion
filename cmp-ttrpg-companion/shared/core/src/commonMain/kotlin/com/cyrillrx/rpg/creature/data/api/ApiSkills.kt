package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiSkills(
    val acrobatics: String?,
    val animalHandling: String?,
    val arcana: String?,
    val athletics: String?,
    val deception: String?,
    val history: String?,
    val insight: String?,
    val intimidation: String?,
    val investigation: String?,
    val medicine: String?,
    val nature: String?,
    val perception: String?,
    val performance: String?,
    val persuasion: String?,
    val religion: String?,
    val sleightOfHand: String?,
    val stealth: String?,
    val survival: String?,
)
