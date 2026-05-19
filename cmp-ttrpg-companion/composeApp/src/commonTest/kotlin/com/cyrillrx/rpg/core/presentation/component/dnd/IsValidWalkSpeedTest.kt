package com.cyrillrx.rpg.core.presentation.component.dnd

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsValidWalkSpeedTest {

    @Test
    fun `returns false for values below 25`() {
        assertFalse(isValidWalkSpeed(0))
        assertFalse(isValidWalkSpeed(20))
        assertFalse(isValidWalkSpeed(24))
    }

    @Test
    fun `returns false for non-multiples of 5`() {
        assertFalse(isValidWalkSpeed(26))
        assertFalse(isValidWalkSpeed(31))
    }

    @Test
    fun `returns true for valid speeds`() {
        assertTrue(isValidWalkSpeed(25))
        assertTrue(isValidWalkSpeed(30))
        assertTrue(isValidWalkSpeed(35))
        assertTrue(isValidWalkSpeed(60))
    }
}
