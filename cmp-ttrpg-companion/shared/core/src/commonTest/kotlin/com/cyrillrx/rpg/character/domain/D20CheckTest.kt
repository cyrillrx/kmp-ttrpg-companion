package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import kotlin.test.Test
import kotlin.test.assertEquals

class D20CheckTest {

    private val character = Character(
        id = "test",
        name = "Test",
        size = Creature.Size.MEDIUM,
        alignment = Creature.Alignment.NEUTRAL,
        abilities = Abilities(
            strength = AbilityScore(10),
            dexterity = AbilityScore(16),
            constitution = AbilityScore(10),
            intelligence = AbilityScore(8),
            wisdom = AbilityScore(14, savingThrowProficiency = Proficiency.PROFICIENT),
            charisma = AbilityScore(10),
        ),
        armorClass = 10,
        maxHitPoints = 10,
        speeds = Speeds(walk = 30),
        languages = emptyList(),
        classes = mapOf(Character.Class.FIGHTER to 5),
        primaryClass = Character.Class.FIGHTER,
        skills = Skills(perception = Proficiency.EXPERT, athletics = Proficiency.PROFICIENT),
    )

    @Test
    fun `a saving throw without proficiency adds the ability modifier alone`() {
        assertEquals(-1, D20Check.SavingThrow(Ability.INTELLIGENCE).modifier(character))
    }

    @Test
    fun `a proficient saving throw adds the proficiency bonus`() {
        assertEquals(5, D20Check.SavingThrow(Ability.WISDOM).modifier(character))
    }

    @Test
    fun `a skill without proficiency adds its ability modifier alone`() {
        assertEquals(-1, D20Check.SkillCheck(Skill.ARCANA).modifier(character))
    }

    @Test
    fun `a proficient skill adds the proficiency bonus`() {
        assertEquals(3, D20Check.SkillCheck(Skill.ATHLETICS).modifier(character))
    }

    @Test
    fun `a skill with expertise adds twice the proficiency bonus`() {
        assertEquals(8, D20Check.SkillCheck(Skill.PERCEPTION).modifier(character))
    }

    @Test
    fun `initiative adds the dexterity modifier`() {
        assertEquals(3, D20Check.Initiative.modifier(character))
    }
}
