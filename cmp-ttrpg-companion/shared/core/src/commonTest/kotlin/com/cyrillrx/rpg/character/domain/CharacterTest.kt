package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
            classes = listOf(
                ClassLevel(Character.Class.ROGUE, 3),
                ClassLevel(Character.Class.WIZARD, 2),
            ),
        )
        assertEquals(5, multiclass.totalLevel)
        assertEquals(3, multiclass.proficiencyBonus())
    }

    @Test
    fun `primaryClass is the class with the most levels`() {
        val multiclass = character().copy(
            classes = listOf(
                ClassLevel(Character.Class.ROGUE, 2),
                ClassLevel(Character.Class.WIZARD, 4),
            ),
        )
        assertEquals(Character.Class.WIZARD, multiclass.primaryClass)
    }

    @Test
    fun `a character without a class has no level and no primary class`() {
        val noClass = character().copy(classes = emptyList())
        assertEquals(0, noClass.totalLevel)
        assertEquals(0, noClass.proficiencyBonus())
        assertNull(noClass.primaryClass)
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
        classes = listOf(ClassLevel(Character.Class.FIGHTER, level)),
        skills = Skills(),
    )
}
