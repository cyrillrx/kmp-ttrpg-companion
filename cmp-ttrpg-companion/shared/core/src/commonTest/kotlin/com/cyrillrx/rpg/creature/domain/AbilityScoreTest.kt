package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AbilityScoreTest {

    @Test
    fun `computeModifier follows the 5e table at bracket boundaries`() {
        assertEquals(-5, AbilityScore.computeModifier(1))
        assertEquals(-4, AbilityScore.computeModifier(2))
        assertEquals(-4, AbilityScore.computeModifier(3))
        assertEquals(-3, AbilityScore.computeModifier(4))
        assertEquals(-1, AbilityScore.computeModifier(9))
        assertEquals(0, AbilityScore.computeModifier(10))
        assertEquals(0, AbilityScore.computeModifier(11))
        assertEquals(1, AbilityScore.computeModifier(12))
        assertEquals(4, AbilityScore.computeModifier(19))
        assertEquals(5, AbilityScore.computeModifier(20))
        assertEquals(9, AbilityScore.computeModifier(29))
        assertEquals(10, AbilityScore.computeModifier(30))
    }

    @Test
    fun `getValueWithModifier formats value and signed modifier`() {
        assertEquals("16 (+3)", AbilityScore(16).getValueWithModifier())
        assertEquals("10 (+0)", AbilityScore(10).getValueWithModifier())
        assertEquals("8 (-1)", AbilityScore(8).getValueWithModifier())
    }

    @Test
    fun `getSavingThrow adds proficiency bonus per proficiency level`() {
        assertEquals(2, AbilityScore(14, Proficiency.NONE).getSavingThrow(3))
        assertEquals(5, AbilityScore(14, Proficiency.PROFICIENT).getSavingThrow(3))
        assertEquals(8, AbilityScore(14, Proficiency.EXPERT).getSavingThrow(3))
    }

    @Test
    fun `getSavingThrow keeps negative modifier when not proficient`() {
        assertEquals(-1, AbilityScore(8, Proficiency.NONE).getSavingThrow(3))
    }
}
