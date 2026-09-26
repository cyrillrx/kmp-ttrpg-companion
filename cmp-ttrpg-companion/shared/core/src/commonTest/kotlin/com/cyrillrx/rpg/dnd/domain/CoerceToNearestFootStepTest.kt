package com.cyrillrx.rpg.dnd.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToNearestFootStepTest {

    @Test
    fun `rounds to the nearest step`() {
        assertEquals(35, 33.coerceToNearestFootStep(min = 0, max = 200))
        assertEquals(30, 32.coerceToNearestFootStep(min = 0, max = 200))
        assertEquals(30, 30.coerceToNearestFootStep(min = 0, max = 200))
    }

    @Test
    fun `clamps to the bounds`() {
        assertEquals(25, 10.coerceToNearestFootStep(min = 25, max = 120))
        assertEquals(120, 500.coerceToNearestFootStep(min = 25, max = 120))
    }

    @Test
    fun `clamps extreme values to the bound they are past`() {
        assertEquals(120, Int.MAX_VALUE.coerceToNearestFootStep(min = 25, max = 120))
        assertEquals(25, Int.MIN_VALUE.coerceToNearestFootStep(min = 25, max = 120))
        assertEquals(200, Int.MAX_VALUE.coerceToNearestFootStep(min = 0, max = 200))
        assertEquals(0, Int.MIN_VALUE.coerceToNearestFootStep(min = 0, max = 200))
    }

    @Test
    fun `rounds within a bound larger than the multiplication can hold`() {
        assertEquals(2147483645, Int.MAX_VALUE.coerceToNearestFootStep(min = 0, max = Int.MAX_VALUE))
    }
}
