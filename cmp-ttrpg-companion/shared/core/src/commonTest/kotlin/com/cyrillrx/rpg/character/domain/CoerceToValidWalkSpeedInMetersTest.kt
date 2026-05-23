package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidWalkSpeedInMetersTest {

    @Test
    fun `clamps values below 7,5 to 7,5`() {
        assertEquals(7.5f, 0f.coerceToValidWalkSpeedInMeters())
        assertEquals(7.5f, 5f.coerceToValidWalkSpeedInMeters())
        assertEquals(7.5f, 7f.coerceToValidWalkSpeedInMeters())  // closer to 7.5 than to 9
    }

    @Test
    fun `clamps values above 36 to 36`() {
        assertEquals(36f, 37f.coerceToValidWalkSpeedInMeters())
        assertEquals(36f, 100f.coerceToValidWalkSpeedInMeters())
    }

    @Test
    fun `rounds to nearest multiple of 1,5`() {
        assertEquals(7.5f, 8f.coerceToValidWalkSpeedInMeters())    // closer to 7.5 than to 9
        assertEquals(10.5f, 10f.coerceToValidWalkSpeedInMeters())   // closer to 10.5 than to 9
        assertEquals(10.5f, 11f.coerceToValidWalkSpeedInMeters())   // closer to 10.5 than to 12
        assertEquals(34.5f, 35f.coerceToValidWalkSpeedInMeters())   // closer to 34.5 than to 36
    }

    @Test
    fun `returns valid values unchanged`() {
        assertEquals(7.5f, 7.5f.coerceToValidWalkSpeedInMeters())
        assertEquals(9f, 9f.coerceToValidWalkSpeedInMeters())
        assertEquals(10.5f, 10.5f.coerceToValidWalkSpeedInMeters())
        assertEquals(18f, 18f.coerceToValidWalkSpeedInMeters())
        assertEquals(36f, 36f.coerceToValidWalkSpeedInMeters())
    }
}
