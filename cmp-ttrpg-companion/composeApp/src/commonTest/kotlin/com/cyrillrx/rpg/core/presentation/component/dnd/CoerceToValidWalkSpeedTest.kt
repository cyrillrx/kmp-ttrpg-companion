package com.cyrillrx.rpg.core.presentation.component.dnd

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidWalkSpeedTest {

    @Test
    fun `clamps values below 25 to 25`() {
        assertEquals(25, 0.coerceToValidWalkSpeed())
        assertEquals(25, 20.coerceToValidWalkSpeed())
        assertEquals(25, 24.coerceToValidWalkSpeed())
    }

    @Test
    fun `rounds to nearest multiple of 5`() {
        assertEquals(25, 26.coerceToValidWalkSpeed())  // remainder 1 → round down
        assertEquals(25, 27.coerceToValidWalkSpeed())  // remainder 2 → round down (boundary)
        assertEquals(30, 28.coerceToValidWalkSpeed())  // remainder 3 → round up
        assertEquals(35, 33.coerceToValidWalkSpeed())  // remainder 3 → round up
    }

    @Test
    fun `returns valid values unchanged`() {
        assertEquals(25, 25.coerceToValidWalkSpeed())
        assertEquals(30, 30.coerceToValidWalkSpeed())
        assertEquals(60, 60.coerceToValidWalkSpeed())
    }
}
