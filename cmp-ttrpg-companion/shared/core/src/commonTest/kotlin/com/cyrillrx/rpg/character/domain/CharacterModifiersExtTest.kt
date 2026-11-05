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

class CharacterModifiersExtTest {

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
    fun `a saving throw proficiency is read from its ability score`() {
        assertEquals(Proficiency.PROFICIENT, character.savingThrowProficiency(Ability.WISDOM))
        assertEquals(Proficiency.NONE, character.savingThrowProficiency(Ability.INTELLIGENCE))
    }

    @Test
    fun `a skill proficiency is read from the character skills`() {
        assertEquals(Proficiency.EXPERT, character.skillProficiency(Skill.PERCEPTION))
        assertEquals(Proficiency.NONE, character.skillProficiency(Skill.ARCANA))
    }

    @Test
    fun `a saving throw without proficiency adds the ability modifier alone`() {
        assertEquals(-1, character.savingThrowModifier(Ability.INTELLIGENCE))
    }

    @Test
    fun `a proficient saving throw adds the proficiency bonus`() {
        assertEquals(5, character.savingThrowModifier(Ability.WISDOM))
    }

    @Test
    fun `a skill without proficiency adds its ability modifier alone`() {
        assertEquals(-1, character.skillModifier(Skill.ARCANA))
    }

    @Test
    fun `a proficient skill adds the proficiency bonus`() {
        assertEquals(3, character.skillModifier(Skill.ATHLETICS))
    }

    @Test
    fun `a skill with expertise adds twice the proficiency bonus`() {
        assertEquals(8, character.skillModifier(Skill.PERCEPTION))
    }

    @Test
    fun `a check reads the modifier of what it names`() {
        assertEquals(5, character.modifierFor(D20Check.SavingThrow(Ability.WISDOM)))
        assertEquals(8, character.modifierFor(D20Check.SkillCheck(Skill.PERCEPTION)))
        assertEquals(3, character.modifierFor(D20Check.Initiative))
    }
}
