package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterTest {

    @Test
    fun `initiativeModifier returns dex modifier`() {
        val fighter = SampleCharacterRepository.humanFighter() // DEX 12 → modifier +1
        assertEquals(1, fighter.initiativeModifier())
    }

    @Test
    fun `initiativeModifier reflects updated dex value`() {
        val highDexCharacter = character(dex = 20) // DEX 20 → modifier +5
        assertEquals(5, highDexCharacter.initiativeModifier())
    }

    @Test
    fun `initiativeModifier is negative for low dex`() {
        val lowDexCharacter = character(dex = 8) // DEX 8 → modifier -1
        assertEquals(-1, lowDexCharacter.initiativeModifier())
    }

    @Test
    fun `initiativeModifier is zero for average dex`() {
        val averageDexCharacter = character(dex = 10) // DEX 10 → modifier 0
        assertEquals(0, averageDexCharacter.initiativeModifier())
    }

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
        description = "",
        size = Creature.Size.MEDIUM,
        alignment = Creature.Alignment.NEUTRAL,
        abilities = Abilities(
            str = Ability(10),
            dex = Ability(dex),
            con = Ability(10),
            int = Ability(10),
            wis = Ability(10),
            cha = Ability(10),
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
