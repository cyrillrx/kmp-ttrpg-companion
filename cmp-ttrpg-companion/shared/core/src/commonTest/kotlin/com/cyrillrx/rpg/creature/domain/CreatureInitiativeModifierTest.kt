package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CreatureInitiativeModifierTest {

    @Test
    fun `initiativeModifier returns dex modifier`() {
        assertEquals(1, creature(dex = 12).initiativeModifier()) // DEX 12 → modifier +1
    }

    @Test
    fun `initiativeModifier reflects updated dex value`() {
        assertEquals(5, creature(dex = 20).initiativeModifier()) // DEX 20 → modifier +5
    }

    @Test
    fun `initiativeModifier is negative for low dex`() {
        assertEquals(-1, creature(dex = 8).initiativeModifier()) // DEX 8 → modifier -1
    }

    @Test
    fun `initiativeModifier is zero for average dex`() {
        assertEquals(0, creature(dex = 10).initiativeModifier()) // DEX 10 → modifier 0
    }

    private fun creature(dex: Int) = object : Creature() {
        override val id = "test"
        override val size = Size.MEDIUM
        override val alignment = Alignment.NEUTRAL
        override val abilities = Abilities(
            strength = AbilityScore(10),
            dexterity = AbilityScore(dex),
            constitution = AbilityScore(10),
            intelligence = AbilityScore(10),
            wisdom = AbilityScore(10),
            charisma = AbilityScore(10),
        )
        override val armorClass = 10
        override val maxHitPoints = 10
        override val speeds = Speeds(walk = 30)
    }
}
