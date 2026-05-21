package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
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

    private fun character(dex: Int = 10, level: Int = 1) = Character(
        id = "test",
        name = "Test",
        size = Creature.Size.MEDIUM,
        alignment = Creature.Alignment.NEUTRAL,
        abilities = Abilities(
            strength = Ability(10),
            dexterity = Ability(dex),
            constitution = Ability(10),
            intelligence = Ability(10),
            wisdom = Ability(10),
            charisma = Ability(10),
        ),
        armorClass = 10,
        maxHitPoints = 10,
        speeds = Speeds(walk = 30),
        languages = emptyList(),
        level = level,
        clazz = Character.Class.FIGHTER,
        skills = Skills(),
    )
}
