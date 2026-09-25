package com.cyrillrx.rpg.core.domain

import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class EntitySortTest {

    private val locale = "en"

    @Test
    fun `last modified puts the most recently updated sheet first`() {
        val sheets = listOf(
            stored("Middle", millis(2_000)),
            stored("Oldest", millis(1_000)),
            stored("Newest", millis(3_000)),
        )

        val sorted = sheets.applySort(StoredSortOrder.LAST_MODIFIED, locale)

        assertEquals(expected = listOf("Newest", "Middle", "Oldest"), actual = sorted.names())
    }

    @Test
    fun `sheets sharing a date are ordered by id`() {
        val sheets = listOf(
            stored("Second", millis(1_000), id = "b"),
            stored("First", millis(1_000), id = "a"),
        )

        val sorted = sheets.applySort(StoredSortOrder.LAST_MODIFIED, locale)

        assertEquals(expected = listOf("a", "b"), actual = sorted.map { it.value.id })
    }

    @Test
    fun `name orders accented sheets as French does`() {
        val sheets = listOf(stored("Roublard", millis(1_000)), stored("Rôdeur", millis(2_000)))

        val sorted = sheets.applySort(StoredSortOrder.NAME, locale)

        assertEquals(expected = listOf("Rôdeur", "Roublard"), actual = sorted.names())
    }

    @Test
    fun `namesakes are ordered by id`() {
        val sheets = listOf(
            stored("Lyra", millis(2_000), id = "second"),
            stored("Lyra", millis(1_000), id = "first"),
        )

        val sorted = sheets.applySort(StoredSortOrder.NAME, locale)

        assertEquals(expected = listOf("first", "second"), actual = sorted.map { it.value.id })
    }

    private fun stored(name: String, updatedAt: Instant, id: String = name) = Stored(
        value = SampleCharacterRepository.humanFighter().copy(id = id, name = name),
        updatedAt = updatedAt,
    )

    private fun millis(epochMillis: Long): Instant = Instant.fromEpochMilliseconds(epochMillis)

    private fun List<Stored<Character>>.names(): List<String> = map { it.value.name }
}
