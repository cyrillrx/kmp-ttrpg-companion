package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsValidWalkSpeedInMetersTest {

    @Test
    fun `returns false for values below 7,5`() {
        assertFalse(isValidWalkSpeedInMeters(0f))
        assertFalse(isValidWalkSpeedInMeters(5f))
        assertFalse(isValidWalkSpeedInMeters(7f))
    }

    @Test
    fun `returns false for non-multiples of 1,5`() {
        assertFalse(isValidWalkSpeedInMeters(8f))
        assertFalse(isValidWalkSpeedInMeters(10f))
        assertFalse(isValidWalkSpeedInMeters(11f))
    }

    @Test
    fun `returns false for values above 36`() {
        assertFalse(isValidWalkSpeedInMeters(36.1f))
        assertFalse(isValidWalkSpeedInMeters(37f))
        assertFalse(isValidWalkSpeedInMeters(100f))
    }

    @Test
    fun `returns true for valid speeds`() {
        assertTrue(isValidWalkSpeedInMeters(7.5f))
        assertTrue(isValidWalkSpeedInMeters(9f))
        assertTrue(isValidWalkSpeedInMeters(10.5f))
        assertTrue(isValidWalkSpeedInMeters(18f))
        assertTrue(isValidWalkSpeedInMeters(36f))
    }
}
