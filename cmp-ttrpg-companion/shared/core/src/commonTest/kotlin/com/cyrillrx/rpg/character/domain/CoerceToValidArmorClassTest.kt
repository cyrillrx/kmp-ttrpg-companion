package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidArmorClassTest {

    @Test
    fun `clamps negative values to 0`() {
        assertEquals(0, (-1).coerceToValidArmorClass())
        assertEquals(0, (-10).coerceToValidArmorClass())
    }

    @Test
    fun `clamps values above 30 to 30`() {
        assertEquals(30, 31.coerceToValidArmorClass())
        assertEquals(30, 100.coerceToValidArmorClass())
    }

    @Test
    fun `returns valid armor class values unchanged`() {
        assertEquals(0, 0.coerceToValidArmorClass())
        assertEquals(10, 10.coerceToValidArmorClass())
        assertEquals(30, 30.coerceToValidArmorClass())
    }
}
