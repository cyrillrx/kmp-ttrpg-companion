package com.cyrillrx.rpg.usercollection.domain

import com.cyrillrx.rpg.core.domain.Entity
import kotlin.test.Test
import kotlin.test.assertEquals

class CollectionItemOrderTest {

    private val locale = "en"

    @Test
    fun `added puts the entry stored last first`() {
        val entries = listOf(Row("a", "Burning Hands"), Row("b", "Mage Hand"), Row("c", "Shield"))

        assertEquals(expected = listOf("c", "b", "a"), actual = entries.orderedIds(CollectionItemOrder.ADDED, locale))
    }

    @Test
    fun `name sorts an accented letter with its base letter`() {
        val entries = listOf(Row("a", "Roublard"), Row("b", "Rôdeur"))

        assertEquals(expected = listOf("b", "a"), actual = entries.orderedIds(CollectionItemOrder.NAME, locale))
    }

    @Test
    fun `name follows the locale it is given`() {
        val entries = listOf(
            Row("a", "Zebra", frenchName = "Alpaga"),
            Row("b", "Alpaca", frenchName = "Zebre"),
        )

        assertEquals(expected = listOf("b", "a"), actual = entries.orderedIds(CollectionItemOrder.NAME, "en"))
        assertEquals(expected = listOf("a", "b"), actual = entries.orderedIds(CollectionItemOrder.NAME, "fr"))
    }

    private fun List<Row>.orderedIds(order: CollectionItemOrder, locale: String): List<String> =
        applyOrder(order, locale).map { it.id }

    private data class Row(
        override val id: String,
        val name: String,
        val frenchName: String = name,
    ) : Entity {
        override fun displayName(locale: String): String = if (locale == "fr") frenchName else name
    }
}
