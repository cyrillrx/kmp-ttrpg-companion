package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidCharacterLevelTest {

    @Test
    fun `clamps values below 1 to 1`() {
        assertEquals(1, 0.coerceToValidCharacterLevel())
        assertEquals(1, (-5).coerceToValidCharacterLevel())
    }

    @Test
    fun `clamps values above 20 to 20`() {
        assertEquals(20, 21.coerceToValidCharacterLevel())
        assertEquals(20, 100.coerceToValidCharacterLevel())
    }

    @Test
    fun `returns valid levels unchanged`() {
        assertEquals(1, 1.coerceToValidCharacterLevel())
        assertEquals(10, 10.coerceToValidCharacterLevel())
        assertEquals(20, 20.coerceToValidCharacterLevel())
    }
}
