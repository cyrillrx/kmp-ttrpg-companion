package com.cyrillrx.rpg.core.presentation.component.dnd

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidAbilityScoreTest {

    @Test
    fun `clamps values below 1 to 1`() {
        assertEquals(1, 0.coerceToValidAbilityScore())
        assertEquals(1, (-5).coerceToValidAbilityScore())
    }

    @Test
    fun `clamps values above 30 to 30`() {
        assertEquals(30, 31.coerceToValidAbilityScore())
        assertEquals(30, 100.coerceToValidAbilityScore())
    }

    @Test
    fun `returns valid ability scores unchanged`() {
        assertEquals(1, 1.coerceToValidAbilityScore())
        assertEquals(10, 10.coerceToValidAbilityScore())
        assertEquals(30, 30.coerceToValidAbilityScore())
    }
}
