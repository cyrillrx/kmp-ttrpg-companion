package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SkillComputeModifierTest {

    private val abilities = Abilities(
        strength = AbilityScore(10),
        dexterity = AbilityScore(14),
        constitution = AbilityScore(10),
        intelligence = AbilityScore(10),
        wisdom = AbilityScore(12),
        charisma = AbilityScore(10),
    )

    @Test
    fun `computeModifier with no proficiency returns ability modifier only`() {
        // Dex modifier = +2, no proficiency bonus
        assertEquals(2, Skill.ACROBATICS.computeModifier(abilities, Proficiency.NONE, 3))
    }

    @Test
    fun `computeModifier with proficiency adds proficiency bonus once`() {
        // Dex modifier = +2, proficiency bonus = 3
        assertEquals(5, Skill.ACROBATICS.computeModifier(abilities, Proficiency.PROFICIENT, 3))
    }

    @Test
    fun `computeModifier with expertise adds proficiency bonus twice`() {
        // Dex modifier = +2, proficiency bonus = 3
        assertEquals(8, Skill.ACROBATICS.computeModifier(abilities, Proficiency.EXPERT, 3))
    }

    @Test
    fun `computeModifier uses the correct related ability`() {
        // Athletics uses Strength (modifier = 0)
        assertEquals(0, Skill.ATHLETICS.computeModifier(abilities, Proficiency.NONE, 3))
        // Perception uses Wisdom (modifier = +1)
        assertEquals(1, Skill.PERCEPTION.computeModifier(abilities, Proficiency.NONE, 3))
    }

    @Test
    fun `computeModifier result can be negative`() {
        val weakAbilities = abilities.copy(dexterity = AbilityScore(8))
        // Dex modifier = -1, no proficiency
        assertEquals(-1, Skill.STEALTH.computeModifier(weakAbilities, Proficiency.NONE, 3))
    }
}
