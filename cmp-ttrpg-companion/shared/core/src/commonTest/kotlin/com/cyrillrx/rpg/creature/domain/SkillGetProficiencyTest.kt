package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SkillGetProficiencyTest {

    @Test
    fun `getProficiency returns the correct field for each skill`() {
        val skills = Skills(
            acrobatics = Proficiency.PROFICIENT,
            animalHandling = Proficiency.EXPERT,
            arcana = Proficiency.PROFICIENT,
            athletics = Proficiency.EXPERT,
            deception = Proficiency.PROFICIENT,
            history = Proficiency.EXPERT,
            insight = Proficiency.PROFICIENT,
            intimidation = Proficiency.EXPERT,
            investigation = Proficiency.PROFICIENT,
            medicine = Proficiency.EXPERT,
            nature = Proficiency.PROFICIENT,
            perception = Proficiency.EXPERT,
            performance = Proficiency.PROFICIENT,
            persuasion = Proficiency.EXPERT,
            religion = Proficiency.PROFICIENT,
            sleightOfHand = Proficiency.EXPERT,
            stealth = Proficiency.PROFICIENT,
            survival = Proficiency.EXPERT,
        )

        assertEquals(Proficiency.PROFICIENT, Skill.ACROBATICS.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.ANIMAL_HANDLING.getProficiency(skills))
        assertEquals(Proficiency.PROFICIENT, Skill.ARCANA.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.ATHLETICS.getProficiency(skills))
        assertEquals(Proficiency.PROFICIENT, Skill.DECEPTION.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.HISTORY.getProficiency(skills))
        assertEquals(Proficiency.PROFICIENT, Skill.INSIGHT.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.INTIMIDATION.getProficiency(skills))
        assertEquals(Proficiency.PROFICIENT, Skill.INVESTIGATION.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.MEDICINE.getProficiency(skills))
        assertEquals(Proficiency.PROFICIENT, Skill.NATURE.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.PERCEPTION.getProficiency(skills))
        assertEquals(Proficiency.PROFICIENT, Skill.PERFORMANCE.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.PERSUASION.getProficiency(skills))
        assertEquals(Proficiency.PROFICIENT, Skill.RELIGION.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.SLEIGHT_OF_HAND.getProficiency(skills))
        assertEquals(Proficiency.PROFICIENT, Skill.STEALTH.getProficiency(skills))
        assertEquals(Proficiency.EXPERT, Skill.SURVIVAL.getProficiency(skills))
    }
}
