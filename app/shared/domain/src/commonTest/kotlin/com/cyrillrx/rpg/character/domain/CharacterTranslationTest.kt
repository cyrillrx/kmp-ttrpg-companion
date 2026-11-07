package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CharacterTranslationTest {

    @Test
    fun `isEmpty returns true for default translation`() {
        assertTrue(Character.Translation().isEmpty())
    }

    @Test
    fun `isEmpty returns true when both fields are empty strings`() {
        assertTrue(Character.Translation(shortDescription = "", description = "").isEmpty())
    }

    @Test
    fun `isEmpty returns true when both fields are whitespace only`() {
        assertTrue(Character.Translation(shortDescription = "   ", description = "  ").isEmpty())
    }

    @Test
    fun `isEmpty returns false when shortDescription is not blank`() {
        assertFalse(Character.Translation(shortDescription = "Hero").isEmpty())
    }

    @Test
    fun `isEmpty returns false when description is not blank`() {
        assertFalse(Character.Translation(description = "A brave warrior").isEmpty())
    }
}
