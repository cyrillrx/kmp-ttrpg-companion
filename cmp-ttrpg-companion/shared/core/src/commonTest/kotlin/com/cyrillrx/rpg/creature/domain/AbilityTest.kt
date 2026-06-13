package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AbilityTest {

    @Test
    fun `computeModifier follows the 5e table at bracket boundaries`() {
        assertEquals(-5, Ability.computeModifier(1))
        assertEquals(-4, Ability.computeModifier(2))
        assertEquals(-4, Ability.computeModifier(3))
        assertEquals(-3, Ability.computeModifier(4))
        assertEquals(-1, Ability.computeModifier(9))
        assertEquals(0, Ability.computeModifier(10))
        assertEquals(0, Ability.computeModifier(11))
        assertEquals(1, Ability.computeModifier(12))
        assertEquals(4, Ability.computeModifier(19))
        assertEquals(5, Ability.computeModifier(20))
        assertEquals(9, Ability.computeModifier(29))
        assertEquals(10, Ability.computeModifier(30))
    }

    @Test
    fun `getValueWithModifier formats value and signed modifier`() {
        assertEquals("16 (+3)", Ability(16).getValueWithModifier())
        assertEquals("10 (+0)", Ability(10).getValueWithModifier())
        assertEquals("8 (-1)", Ability(8).getValueWithModifier())
    }

    @Test
    fun `getSavingThrow adds proficiency bonus per proficiency level`() {
        assertEquals(2, Ability(14, Proficiency.NONE).getSavingThrow(3))
        assertEquals(5, Ability(14, Proficiency.PROFICIENT).getSavingThrow(3))
        assertEquals(8, Ability(14, Proficiency.EXPERT).getSavingThrow(3))
    }

    @Test
    fun `getSavingThrow keeps negative modifier when not proficient`() {
        assertEquals(-1, Ability(8, Proficiency.NONE).getSavingThrow(3))
    }
}
