package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SkillGetRelatedAbilityTest {

    @Test
    fun `getRelatedAbility returns the correct ability for each skill`() {
        val expected = mapOf(
            Skill.ACROBATICS to Ability.DEXTERITY,
            Skill.ANIMAL_HANDLING to Ability.WISDOM,
            Skill.ARCANA to Ability.INTELLIGENCE,
            Skill.ATHLETICS to Ability.STRENGTH,
            Skill.DECEPTION to Ability.CHARISMA,
            Skill.HISTORY to Ability.INTELLIGENCE,
            Skill.INSIGHT to Ability.WISDOM,
            Skill.INTIMIDATION to Ability.CHARISMA,
            Skill.INVESTIGATION to Ability.INTELLIGENCE,
            Skill.MEDICINE to Ability.WISDOM,
            Skill.NATURE to Ability.INTELLIGENCE,
            Skill.PERCEPTION to Ability.WISDOM,
            Skill.PERFORMANCE to Ability.CHARISMA,
            Skill.PERSUASION to Ability.CHARISMA,
            Skill.RELIGION to Ability.INTELLIGENCE,
            Skill.SLEIGHT_OF_HAND to Ability.DEXTERITY,
            Skill.STEALTH to Ability.DEXTERITY,
            Skill.SURVIVAL to Ability.WISDOM,
        )

        Skill.entries.forEach { skill ->
            assertEquals(expected.getValue(skill), skill.getRelatedAbility(), "Wrong related ability for $skill")
        }
    }
}
