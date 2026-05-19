package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidMaxHitPointsTest {

    @Test
    fun `clamps values below 1 to 1`() {
        assertEquals(1, 0.coerceToValidMaxHitPoints())
        assertEquals(1, (-5).coerceToValidMaxHitPoints())
    }

    @Test
    fun `returns valid max hit points unchanged`() {
        assertEquals(1, 1.coerceToValidMaxHitPoints())
        assertEquals(10, 10.coerceToValidMaxHitPoints())
        assertEquals(100, 100.coerceToValidMaxHitPoints())
    }
}
