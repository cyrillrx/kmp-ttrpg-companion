package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.domain.UNKNOWN_TIMESTAMP
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class CharacterSortOrderTest {

    @Test
    fun `last modified puts the most recently updated sheet first`() {
        val sheets = listOf(
            stored("Middle", millis(2_000)),
            stored("Oldest", millis(1_000)),
            stored("Newest", millis(3_000)),
        )

        val sorted = sheets.applySort(CharacterSortOrder.LAST_MODIFIED)

        assertEquals(expected = listOf("Newest", "Middle", "Oldest"), actual = sorted.names())
    }

    @Test
    fun `last modified sinks the sheets with no known date`() {
        val sheets = listOf(stored("Undated", UNKNOWN_TIMESTAMP), stored("Dated", millis(1_000)))

        val sorted = sheets.applySort(CharacterSortOrder.LAST_MODIFIED)

        assertEquals(expected = listOf("Dated", "Undated"), actual = sorted.names())
    }

    @Test
    fun `last modified breaks ties on the localized name`() {
        val sheets = listOf(stored("Roublard", millis(1_000)), stored("Rôdeur", millis(1_000)))

        val sorted = sheets.applySort(CharacterSortOrder.LAST_MODIFIED)

        assertEquals(expected = listOf("Rôdeur", "Roublard"), actual = sorted.names())
    }

    @Test
    fun `name orders accented sheets as French does`() {
        val sheets = listOf(stored("Roublard", millis(1_000)), stored("Rôdeur", millis(2_000)))

        val sorted = sheets.applySort(CharacterSortOrder.NAME)

        assertEquals(expected = listOf("Rôdeur", "Roublard"), actual = sorted.names())
    }

    @Test
    fun `name puts the most recent namesake first`() {
        val sheets = listOf(
            stored("Lyra", millis(1_000), id = "older"),
            stored("Lyra", millis(2_000), id = "newer"),
        )

        val sorted = sheets.applySort(CharacterSortOrder.NAME)

        assertEquals(expected = listOf("newer", "older"), actual = sorted.map { it.value.id })
    }

    @Test
    fun `namesakes updated at the same time are ordered by id`() {
        val sheets = listOf(
            stored("Lyra", millis(1_000), id = "second"),
            stored("Lyra", millis(1_000), id = "first"),
        )

        val sorted = sheets.applySort(CharacterSortOrder.NAME)

        assertEquals(expected = listOf("first", "second"), actual = sorted.map { it.value.id })
    }

    private fun stored(name: String, updatedAt: Instant, id: String = name) = Stored(
        value = SampleCharacterRepository.humanFighter().copy(id = id, name = name),
        updatedAt = updatedAt,
    )

    private fun millis(epochMillis: Long): Instant = Instant.fromEpochMilliseconds(epochMillis)

    private fun List<Stored<Character>>.names(): List<String> = map { it.value.name }
}
