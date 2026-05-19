package com.cyrillrx.rpg.core.presentation.component.dnd

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsValidCharacterLevelTest {

    @Test
    fun `returns false for values below 1`() {
        assertFalse(isValidCharacterLevel(0))
        assertFalse(isValidCharacterLevel(-1))
    }

    @Test
    fun `returns false for values above 20`() {
        assertFalse(isValidCharacterLevel(21))
        assertFalse(isValidCharacterLevel(100))
    }

    @Test
    fun `returns true for valid levels`() {
        assertTrue(isValidCharacterLevel(1))
        assertTrue(isValidCharacterLevel(10))
        assertTrue(isValidCharacterLevel(20))
    }
}
