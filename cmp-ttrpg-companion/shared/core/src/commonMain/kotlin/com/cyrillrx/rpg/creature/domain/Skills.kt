package com.cyrillrx.rpg.creature.domain

enum class Proficiency { NONE, PROFICIENT, EXPERT }

data class Skills(
    val acrobatics: Proficiency = Proficiency.NONE,
    val animalHandling: Proficiency = Proficiency.NONE,
    val arcana: Proficiency = Proficiency.NONE,
    val athletics: Proficiency = Proficiency.NONE,
    val deception: Proficiency = Proficiency.NONE,
    val history: Proficiency = Proficiency.NONE,
    val insight: Proficiency = Proficiency.NONE,
    val intimidation: Proficiency = Proficiency.NONE,
    val investigation: Proficiency = Proficiency.NONE,
    val medicine: Proficiency = Proficiency.NONE,
    val nature: Proficiency = Proficiency.NONE,
    val perception: Proficiency = Proficiency.NONE,
    val performance: Proficiency = Proficiency.NONE,
    val persuasion: Proficiency = Proficiency.NONE,
    val religion: Proficiency = Proficiency.NONE,
    val sleightOfHand: Proficiency = Proficiency.NONE,
    val stealth: Proficiency = Proficiency.NONE,
    val survival: Proficiency = Proficiency.NONE,
)
