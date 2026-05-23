package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsValidWalkSpeedInFeetTest {

    @Test
    fun `returns false for values below 25`() {
        assertFalse(isValidWalkSpeedInFeet(0))
        assertFalse(isValidWalkSpeedInFeet(20))
        assertFalse(isValidWalkSpeedInFeet(24))
    }

    @Test
    fun `returns false for non-multiples of 5`() {
        assertFalse(isValidWalkSpeedInFeet(26))
        assertFalse(isValidWalkSpeedInFeet(31))
    }

    @Test
    fun `returns false for values above 120`() {
        assertFalse(isValidWalkSpeedInFeet(125))
        assertFalse(isValidWalkSpeedInFeet(200))
    }

    @Test
    fun `returns true for valid speeds`() {
        assertTrue(isValidWalkSpeedInFeet(25))
        assertTrue(isValidWalkSpeedInFeet(30))
        assertTrue(isValidWalkSpeedInFeet(35))
        assertTrue(isValidWalkSpeedInFeet(60))
        assertTrue(isValidWalkSpeedInFeet(120))
    }
}
