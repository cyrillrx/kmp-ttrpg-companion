package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MonsterTest {

    @Test
    fun `construction fails when types is empty`() {
        assertFailsWith<IllegalArgumentException> {
            validMonster(types = emptySet())
        }
    }

    @Test
    fun `construction fails when translations is empty`() {
        assertFailsWith<IllegalArgumentException> {
            validMonster(translations = emptyMap())
        }
    }

    @Test
    fun `getPrimaryType returns the first type in declaration order`() {
        val monster = validMonster(types = setOf(Monster.Type.CELESTIAL, Monster.Type.FIEND))
        assertEquals(Monster.Type.CELESTIAL, monster.getPrimaryType())
    }

    private fun validMonster(
        types: Set<Monster.Type> = hashSetOf(Monster.Type.BEAST),
        translations: Map<String, Monster.Translation> = mapOf(
            "en" to Monster.Translation(
                name = "Test",
                description = "A test.",
                senses = "Darkvision 60 ft.",
                languages = emptyList(),
            ),
        ),
    ) = Monster(
        id = "test",
        source = "test",
        types = types,
        size = Creature.Size.MEDIUM,
        alignment = Creature.Alignment.NEUTRAL,
        challengeRating = 1f,
        hitDice = "1d8",
        abilities = Abilities(
            strength = Ability(10),
            dexterity = Ability(10),
            constitution = Ability(10),
            intelligence = Ability(10),
            wisdom = Ability(10),
            charisma = Ability(10),
        ),
        armorClass = 10,
        maxHitPoints = 10,
        speeds = Speeds(walk = 30),
        translations = translations,
    )
}
