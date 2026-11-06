package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AbilityToAbilityScoreTest {

    private val abilities = Abilities(
        strength = AbilityScore(8),
        dexterity = AbilityScore(10),
        constitution = AbilityScore(12),
        intelligence = AbilityScore(14),
        wisdom = AbilityScore(16),
        charisma = AbilityScore(18),
    )

    @Test
    fun `each ability reads its own score`() {
        val expected = mapOf(
            Ability.STRENGTH to abilities.strength,
            Ability.DEXTERITY to abilities.dexterity,
            Ability.CONSTITUTION to abilities.constitution,
            Ability.INTELLIGENCE to abilities.intelligence,
            Ability.WISDOM to abilities.wisdom,
            Ability.CHARISMA to abilities.charisma,
        )

        Ability.entries.forEach { ability ->
            assertEquals(expected.getValue(ability), ability.toAbilityScore(abilities), "$ability")
        }
    }
}
