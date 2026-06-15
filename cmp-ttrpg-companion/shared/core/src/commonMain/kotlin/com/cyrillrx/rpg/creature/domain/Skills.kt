package com.cyrillrx.rpg.creature.domain

import kotlinx.serialization.Serializable

@Serializable
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
) {
    fun applySelection(selected: Map<Skill, Proficiency>): Skills =
        selected.entries.fold(this) { acc, (skill, proficiency) -> acc.withProficiency(skill, proficiency) }

    private fun withProficiency(skill: Skill, proficiency: Proficiency): Skills = when (skill) {
        Skill.ACROBATICS -> copy(acrobatics = proficiency)
        Skill.ANIMAL_HANDLING -> copy(animalHandling = proficiency)
        Skill.ARCANA -> copy(arcana = proficiency)
        Skill.ATHLETICS -> copy(athletics = proficiency)
        Skill.DECEPTION -> copy(deception = proficiency)
        Skill.HISTORY -> copy(history = proficiency)
        Skill.INSIGHT -> copy(insight = proficiency)
        Skill.INTIMIDATION -> copy(intimidation = proficiency)
        Skill.INVESTIGATION -> copy(investigation = proficiency)
        Skill.MEDICINE -> copy(medicine = proficiency)
        Skill.NATURE -> copy(nature = proficiency)
        Skill.PERCEPTION -> copy(perception = proficiency)
        Skill.PERFORMANCE -> copy(performance = proficiency)
        Skill.PERSUASION -> copy(persuasion = proficiency)
        Skill.RELIGION -> copy(religion = proficiency)
        Skill.SLEIGHT_OF_HAND -> copy(sleightOfHand = proficiency)
        Skill.STEALTH -> copy(stealth = proficiency)
        Skill.SURVIVAL -> copy(survival = proficiency)
    }
}
