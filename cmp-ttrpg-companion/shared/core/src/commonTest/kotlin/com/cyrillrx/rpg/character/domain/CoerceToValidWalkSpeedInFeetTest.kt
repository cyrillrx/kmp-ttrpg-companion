package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidWalkSpeedInFeetTest {

    @Test
    fun `clamps values below 25 to 25`() {
        assertEquals(25, 0.coerceToValidWalkSpeedInFeet())
        assertEquals(25, 20.coerceToValidWalkSpeedInFeet())
        assertEquals(25, 24.coerceToValidWalkSpeedInFeet())
    }

    @Test
    fun `clamps values above 120 to 120`() {
        assertEquals(120, 121.coerceToValidWalkSpeedInFeet())
        assertEquals(120, 200.coerceToValidWalkSpeedInFeet())
    }

    @Test
    fun `rounds to nearest multiple of 5`() {
        assertEquals(25, 26.coerceToValidWalkSpeedInFeet()) // remainder 1 → round down
        assertEquals(25, 27.coerceToValidWalkSpeedInFeet()) // remainder 2 → round down (boundary)
        assertEquals(30, 28.coerceToValidWalkSpeedInFeet()) // remainder 3 → round up
        assertEquals(35, 33.coerceToValidWalkSpeedInFeet()) // remainder 3 → round up
        assertEquals(115, 117.coerceToValidWalkSpeedInFeet()) // remainder 2 → round down
        assertEquals(120, 118.coerceToValidWalkSpeedInFeet()) // remainder 3 → round up to upper bound
        assertEquals(120, 119.coerceToValidWalkSpeedInFeet()) // remainder 4 → round up to upper bound
    }

    @Test
    fun `returns valid values unchanged`() {
        assertEquals(25, 25.coerceToValidWalkSpeedInFeet())
        assertEquals(30, 30.coerceToValidWalkSpeedInFeet())
        assertEquals(60, 60.coerceToValidWalkSpeedInFeet())
        assertEquals(120, 120.coerceToValidWalkSpeedInFeet())
    }
}
