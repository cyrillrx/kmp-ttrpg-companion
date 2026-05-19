package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsValidAbilityScoreTest {

    @Test
    fun `returns false for values below 1`() {
        assertFalse(isValidAbilityScore(0))
        assertFalse(isValidAbilityScore(-1))
    }

    @Test
    fun `returns false for values above 30`() {
        assertFalse(isValidAbilityScore(31))
        assertFalse(isValidAbilityScore(100))
    }

    @Test
    fun `returns true for valid ability scores`() {
        assertTrue(isValidAbilityScore(1))
        assertTrue(isValidAbilityScore(10))
        assertTrue(isValidAbilityScore(30))
    }
}
