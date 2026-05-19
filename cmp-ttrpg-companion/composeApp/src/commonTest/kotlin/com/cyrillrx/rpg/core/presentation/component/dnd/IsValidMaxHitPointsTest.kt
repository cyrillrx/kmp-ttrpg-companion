package com.cyrillrx.rpg.core.presentation.component.dnd

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsValidMaxHitPointsTest {

    @Test
    fun `returns false for values below 1`() {
        assertFalse(isValidMaxHitPoints(0))
        assertFalse(isValidMaxHitPoints(-1))
    }

    @Test
    fun `returns true for valid max hit points`() {
        assertTrue(isValidMaxHitPoints(1))
        assertTrue(isValidMaxHitPoints(10))
        assertTrue(isValidMaxHitPoints(100))
    }
}
