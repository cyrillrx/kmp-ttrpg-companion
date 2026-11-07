package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterTest {

    @Test
    fun `proficiencyBonus is 2 for levels 1 to 4`() {
        assertEquals(2, character(level = 1).proficiencyBonus())
        assertEquals(2, character(level = 4).proficiencyBonus())
    }

    @Test
    fun `proficiencyBonus is 3 for levels 5 to 8`() {
        assertEquals(3, character(level = 5).proficiencyBonus())
        assertEquals(3, character(level = 8).proficiencyBonus())
    }

    @Test
    fun `proficiencyBonus is 6 for levels 17 to 20`() {
        assertEquals(6, character(level = 17).proficiencyBonus())
        assertEquals(6, character(level = 20).proficiencyBonus())
    }

    @Test
    fun `totalLevel sums every class level and drives the proficiency bonus`() {
        val multiclass = character().copy(
            classes = mapOf(
                Character.Class.ROGUE to 3,
                Character.Class.WIZARD to 2,
            ),
            primaryClass = Character.Class.ROGUE,
        )
        assertEquals(5, multiclass.totalLevel)
        assertEquals(3, multiclass.proficiencyBonus())
    }

    @Test
    fun `proficiencyBonus stays at 6 when the class levels sum above 20`() {
        val overTwenty = character().copy(
            classes = mapOf(
                Character.Class.FIGHTER to 20,
                Character.Class.ROGUE to 2,
            ),
        )
        assertEquals(22, overTwenty.totalLevel)
        assertEquals(6, overTwenty.proficiencyBonus())
    }

    @Test
    fun `an unspecified class still carries a level and a proficiency bonus`() {
        val unspecified = character().copy(
            classes = mapOf(Character.Class.UNKNOWN to 1),
            primaryClass = Character.Class.UNKNOWN,
        )
        assertEquals(1, unspecified.totalLevel)
        assertEquals(2, unspecified.proficiencyBonus())
    }

    private fun character(dex: Int = 10, level: Int = 1) = Character(
        id = "test",
        name = "Test",
        size = Creature.Size.MEDIUM,
        alignment = Creature.Alignment.NEUTRAL,
        abilities = Abilities(
            strength = AbilityScore(10),
            dexterity = AbilityScore(dex),
            constitution = AbilityScore(10),
            intelligence = AbilityScore(10),
            wisdom = AbilityScore(10),
            charisma = AbilityScore(10),
        ),
        armorClass = 10,
        maxHitPoints = 10,
        speeds = Speeds(walk = 30),
        languages = emptyList(),
        classes = mapOf(Character.Class.FIGHTER to level),
        primaryClass = Character.Class.FIGHTER,
        skills = Skills(),
    )
}
