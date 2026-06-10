package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

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
    fun `getDisplayType returns one of the monster's types`() {
        val monster = validMonster(types = setOf(Monster.Type.CELESTIAL, Monster.Type.FIEND))
        assertTrue(monster.getDisplayType() in monster.types)
    }

    private fun validMonster(
        types: Set<Monster.Type> = setOf(Monster.Type.BEAST),
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
