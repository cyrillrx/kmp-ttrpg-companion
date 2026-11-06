package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidArmorClassTest {

    @Test
    fun `clamps values below 1 to 1`() {
        assertEquals(1, 0.coerceToValidArmorClass())
        assertEquals(1, (-1).coerceToValidArmorClass())
        assertEquals(1, (-10).coerceToValidArmorClass())
    }

    @Test
    fun `clamps values above 40 to 40`() {
        assertEquals(40, 41.coerceToValidArmorClass())
        assertEquals(40, 100.coerceToValidArmorClass())
    }

    @Test
    fun `returns valid armor class values unchanged`() {
        assertEquals(1, 1.coerceToValidArmorClass())
        assertEquals(10, 10.coerceToValidArmorClass())
        assertEquals(40, 40.coerceToValidArmorClass())
    }
}
