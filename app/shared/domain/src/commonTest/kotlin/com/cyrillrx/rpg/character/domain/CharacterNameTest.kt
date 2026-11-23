package com.cyrillrx.rpg.character.domain

import com.cyrillrx.core.data.defaultSerializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class CharacterNameTest {

    @Test
    fun `parse trims surrounding whitespace`() {
        assertEquals(CharacterName("Thordak"), CharacterName.parse(" Thordak \n"))
    }

    @Test
    fun `a blank name has no valid form`() {
        assertNull(CharacterName.parse("  \t"))
    }

    @Test
    fun `an empty name has no valid form`() {
        assertNull(CharacterName.parse(""))
    }

    @Test
    fun `parse truncates a name longer than the maximum`() {
        val parsed = CharacterName.parse("a".repeat(MAX_CHARACTER_NAME_LENGTH + 10))

        assertEquals("a".repeat(MAX_CHARACTER_NAME_LENGTH), parsed?.value)
    }

    @Test
    fun `parse drops the whitespace a truncation leaves at the end`() {
        val parsed = CharacterName.parse("a".repeat(MAX_CHARACTER_NAME_LENGTH - 1) + " bcd")

        assertEquals("a".repeat(MAX_CHARACTER_NAME_LENGTH - 1), parsed?.value)
    }

    @Test
    fun `parse drops an emoji the maximum would split`() {
        val parsed = CharacterName.parse("a".repeat(MAX_CHARACTER_NAME_LENGTH - 1) + "🐉")

        assertEquals("a".repeat(MAX_CHARACTER_NAME_LENGTH - 1), parsed?.value)
    }

    @Test
    fun `an invalid name cannot be built`() {
        assertFailsWith<IllegalArgumentException> { CharacterName(" Thordak") }
    }

    @Test
    fun `a stored name is repaired on decoding`() {
        assertEquals(CharacterName("Thordak"), defaultSerializer.decodeFromString<CharacterName>("\" Thordak \""))
    }

    @Test
    fun `a blank stored name decodes to a placeholder`() {
        assertEquals(CharacterName("?"), defaultSerializer.decodeFromString<CharacterName>("\"  \""))
    }
}
