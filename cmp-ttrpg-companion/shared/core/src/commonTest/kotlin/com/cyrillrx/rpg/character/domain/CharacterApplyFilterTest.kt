package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CharacterApplyFilterTest {

    private val characters = SampleCharacterRepository.getAll()

    @Test
    fun `null filter returns the full list`() {
        val result = characters.applyFilter(null)
        assertEquals(characters, result)
    }

    @Test
    fun `default filter returns all characters`() {
        val result = characters.applyFilter(CharacterFilter())
        assertEquals(characters.size, result.size)
    }

    @Test
    fun `filter by query keeps only matching characters`() {
        val result = characters.applyFilter(CharacterFilter(query = "Lyra"))
        assertEquals(1, result.size)
        assertEquals("Lyra Vossen", result.first().name)
    }

    @Test
    fun `filter by query is case insensitive`() {
        val result = characters.applyFilter(CharacterFilter(query = "lyra"))
        assertEquals(1, result.size)
    }

    @Test
    fun `filter with no match returns empty list`() {
        val result = characters.applyFilter(CharacterFilter(query = "Gandalf"))
        assertTrue(result.isEmpty())
    }
}
