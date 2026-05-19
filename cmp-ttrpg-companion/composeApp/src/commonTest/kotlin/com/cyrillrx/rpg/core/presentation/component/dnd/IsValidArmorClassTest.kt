package com.cyrillrx.rpg.core.presentation.component.dnd

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsValidArmorClassTest {

    @Test
    fun `returns false for negative values`() {
        assertFalse(isValidArmorClass(-1))
        assertFalse(isValidArmorClass(-10))
    }

    @Test
    fun `returns true for valid armor class values`() {
        assertTrue(isValidArmorClass(0))
        assertTrue(isValidArmorClass(10))
        assertTrue(isValidArmorClass(30))
    }
}
