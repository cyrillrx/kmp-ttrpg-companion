package com.cyrillrx.rpg.core.presentation.component.dnd

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidWalkSpeedTest {

    @Test
    fun `clamps values below 25 to 25`() {
        assertEquals(25, coerceToValidWalkSpeed(0))
        assertEquals(25, coerceToValidWalkSpeed(20))
        assertEquals(25, coerceToValidWalkSpeed(24))
    }

    @Test
    fun `rounds to nearest multiple of 5`() {
        assertEquals(25, coerceToValidWalkSpeed(26))
        assertEquals(30, coerceToValidWalkSpeed(28))
        assertEquals(35, coerceToValidWalkSpeed(33))
    }

    @Test
    fun `returns valid values unchanged`() {
        assertEquals(25, coerceToValidWalkSpeed(25))
        assertEquals(30, coerceToValidWalkSpeed(30))
        assertEquals(60, coerceToValidWalkSpeed(60))
    }
}
