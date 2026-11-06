package com.cyrillrx.rpg.core.domain

import com.cyrillrx.rpg.usercollection.domain.UserCollection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class EntitySortTest {

    private val locale = "en"

    @Test
    fun `last modified puts the most recently updated record first`() {
        val records = listOf(
            stored("Middle", millis(2_000)),
            stored("Oldest", millis(1_000)),
            stored("Newest", millis(3_000)),
        )

        val sorted = records.applySort(StoredSortOrder.LAST_MODIFIED, locale)

        assertEquals(expected = listOf("Newest", "Middle", "Oldest"), actual = sorted.names())
    }

    @Test
    fun `records sharing a date are ordered by id`() {
        val records = listOf(
            stored("Second", millis(1_000), id = "b"),
            stored("First", millis(1_000), id = "a"),
        )

        val sorted = records.applySort(StoredSortOrder.LAST_MODIFIED, locale)

        assertEquals(expected = listOf("a", "b"), actual = sorted.map { it.value.id })
    }

    @Test
    fun `name orders accented records as French does`() {
        val records = listOf(stored("Roublard", millis(1_000)), stored("Rôdeur", millis(2_000)))

        val sorted = records.applySort(StoredSortOrder.NAME, locale)

        assertEquals(expected = listOf("Rôdeur", "Roublard"), actual = sorted.names())
    }

    @Test
    fun `namesakes are ordered by id`() {
        val records = listOf(
            stored("Lyra", millis(2_000), id = "second"),
            stored("Lyra", millis(1_000), id = "first"),
        )

        val sorted = records.applySort(StoredSortOrder.NAME, locale)

        assertEquals(expected = listOf("first", "second"), actual = sorted.map { it.value.id })
    }

    @Test
    fun `name follows the locale it is given`() {
        val records = listOf(
            stored("Zebra", millis(1_000), id = "a", frenchName = "Alpaga"),
            stored("Alpaca", millis(2_000), id = "b", frenchName = "Zebre"),
        )

        assertEquals(expected = listOf("b", "a"), actual = records.applySort(StoredSortOrder.NAME, "en").ids())
        assertEquals(expected = listOf("a", "b"), actual = records.applySort(StoredSortOrder.NAME, "fr").ids())
    }

    @Test
    fun `sortedByName breaks ties between namesakes by id`() {
        val records = listOf(Row("second", "Lyra"), Row("first", "Lyra"), Row("c", "Elandra"))

        val sorted = records.sortedByName(locale)

        assertEquals(expected = listOf("c", "first", "second"), actual = sorted.map { it.id })
    }

    @Test
    fun `the order applies to any stored entity`() {
        val collections = listOf(
            Stored(collection(id = "b", name = "Rituals"), millis(2_000)),
            Stored(collection(id = "a", name = "Cantrips"), millis(1_000)),
        )

        val sorted = collections.applySort(StoredSortOrder.NAME, locale)

        assertEquals(expected = listOf("Cantrips", "Rituals"), actual = sorted.map { it.value.name })
    }

    private fun stored(name: String, updatedAt: Instant, id: String = name, frenchName: String = name) =
        Stored(value = Row(id = id, name = name, frenchName = frenchName), updatedAt = updatedAt)

    private fun collection(id: String, name: String) =
        UserCollection(id = id, name = name, itemType = UserCollection.ItemType.SPELL, itemIds = emptyList())

    private fun millis(epochMillis: Long): Instant = Instant.fromEpochMilliseconds(epochMillis)

    private fun List<Stored<Row>>.names(): List<String> = map { it.value.name }

    private fun List<Stored<Row>>.ids(): List<String> = map { it.value.id }

    private data class Row(
        override val id: String,
        val name: String,
        val frenchName: String = name,
    ) : Entity {
        override fun displayName(locale: String): String = if (locale == "fr") frenchName else name
    }
}
