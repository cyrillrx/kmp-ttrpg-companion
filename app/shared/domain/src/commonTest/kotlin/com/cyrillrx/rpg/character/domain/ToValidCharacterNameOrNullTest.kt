package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ToValidCharacterNameOrNullTest {

    @Test
    fun `trims surrounding whitespace`() {
        assertEquals("Thordak", " Thordak \n".toValidCharacterNameOrNull())
    }

    @Test
    fun `a blank name has no valid form`() {
        assertNull("  \t".toValidCharacterNameOrNull())
    }

    @Test
    fun `an empty name has no valid form`() {
        assertNull("".toValidCharacterNameOrNull())
    }
}
