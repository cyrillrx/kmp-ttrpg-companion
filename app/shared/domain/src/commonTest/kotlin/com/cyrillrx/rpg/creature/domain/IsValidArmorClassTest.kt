package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsValidArmorClassTest {

    @Test
    fun `returns false for values below 1`() {
        assertFalse(isValidArmorClass(0))
        assertFalse(isValidArmorClass(-1))
        assertFalse(isValidArmorClass(-10))
    }

    @Test
    fun `returns false for values above 40`() {
        assertFalse(isValidArmorClass(41))
        assertFalse(isValidArmorClass(100))
    }

    @Test
    fun `returns true for valid armor class values`() {
        assertTrue(isValidArmorClass(1))
        assertTrue(isValidArmorClass(10))
        assertTrue(isValidArmorClass(40))
    }
}
