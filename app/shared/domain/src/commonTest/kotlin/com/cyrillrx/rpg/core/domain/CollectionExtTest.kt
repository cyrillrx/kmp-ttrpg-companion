package com.cyrillrx.rpg.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CollectionExtTest {

    @Test
    fun `toggled adds item when absent`() {
        val initialSet = setOf("a", "b")
        val setAfterToggle = initialSet.toggled("c")
        assertEquals(
            expected = setOf("a", "b", "c"),
            actual = setAfterToggle,
        )
    }

    @Test
    fun `toggled removes item when present`() {
        val initialSet = setOf("a", "b", "c")
        val setAfterToggle = initialSet.toggled("b")
        assertEquals(
            expected = setOf("a", "c"),
            actual = setAfterToggle,
        )
    }

    @Test
    fun `toggled adds item to empty set`() {
        val initialSet = emptySet<String>()
        val setAfterToggle = initialSet.toggled("x")
        assertEquals(
            expected = setOf(element = "x"),
            actual = setAfterToggle,
        )
    }
}
