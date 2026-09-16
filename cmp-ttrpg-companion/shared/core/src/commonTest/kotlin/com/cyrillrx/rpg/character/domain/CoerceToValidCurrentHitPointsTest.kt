package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidCurrentHitPointsTest {

    @Test
    fun `clamps negative values to 0`() {
        assertEquals(0, (-1).coerceToValidCurrentHitPoints(maxHitPoints = 12))
        assertEquals(0, (-30).coerceToValidCurrentHitPoints(maxHitPoints = 12))
    }

    @Test
    fun `clamps values above the maximum to the maximum`() {
        assertEquals(12, 13.coerceToValidCurrentHitPoints(maxHitPoints = 12))
        assertEquals(12, 500.coerceToValidCurrentHitPoints(maxHitPoints = 12))
    }

    @Test
    fun `returns valid current hit points unchanged`() {
        assertEquals(0, 0.coerceToValidCurrentHitPoints(maxHitPoints = 12))
        assertEquals(7, 7.coerceToValidCurrentHitPoints(maxHitPoints = 12))
        assertEquals(12, 12.coerceToValidCurrentHitPoints(maxHitPoints = 12))
    }
}
