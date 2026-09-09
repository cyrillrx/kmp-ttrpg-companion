package com.cyrillrx.rpg.core.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Compared by reference, like the compendium entities the collection detail screen renders. */
private class Row(val id: String)

private fun deletions() = OptimisticDeletions<Row> { it.id }

class OptimisticDeletionsTest {

    private val first = Row("1")
    private val second = Row("2")

    @Test
    fun `visible is the loaded list when nothing is hidden`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        assertEquals(expected = listOf("1", "2"), actual = subject.visible.map { it.id })
    }

    @Test
    fun `hide removes the item from visible`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        subject.hide(first)

        assertEquals(expected = listOf("2"), actual = subject.visible.map { it.id })
    }

    @Test
    fun `undo restores the item`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        val pending = assertNotNull(subject.hide(first))

        assertTrue(subject.undo(pending))
        assertEquals(expected = listOf("1", "2"), actual = subject.visible.map { it.id })
    }

    @Test
    fun `a claimed item stays hidden while the repository call is in flight`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        val pending = assertNotNull(subject.hide(first))
        assertTrue(subject.claim(pending))

        assertEquals(expected = listOf("2"), actual = subject.visible.map { it.id })
    }

    @Test
    fun `a reload landing during the commit does not bring the claimed item back`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        val pending = assertNotNull(subject.hide(first))
        subject.claim(pending)
        subject.setLoaded(listOf(first, second))

        assertEquals(expected = listOf("2"), actual = subject.visible.map { it.id })
    }

    @Test
    fun `an item is recognised across two reads that rebuilt it`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        subject.hide(first)
        subject.setLoaded(listOf(Row("1"), Row("2")))

        assertEquals(expected = listOf("2"), actual = subject.visible.map { it.id })
    }

    @Test
    fun `undo after claim is refused`() {
        val subject = deletions()
        subject.setLoaded(listOf(first))

        val pending = assertNotNull(subject.hide(first))
        subject.claim(pending)

        assertFalse(subject.undo(pending))
    }

    @Test
    fun `claim after undo is refused`() {
        val subject = deletions()
        subject.setLoaded(listOf(first))

        val pending = assertNotNull(subject.hide(first))
        subject.undo(pending)

        assertFalse(subject.claim(pending))
    }

    @Test
    fun `claim is one-shot`() {
        val subject = deletions()
        subject.setLoaded(listOf(first))

        val pending = assertNotNull(subject.hide(first))

        assertTrue(subject.claim(pending))
        assertFalse(subject.claim(pending))
    }

    @Test
    fun `settling a successful commit drops the item from the loaded list`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        val pending = assertNotNull(subject.hide(first))
        subject.claim(pending)
        subject.settle(pending, deleted = true)

        assertEquals(expected = listOf("2"), actual = subject.visible.map { it.id })
    }

    @Test
    fun `settling a failed commit makes the item visible again`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        val pending = assertNotNull(subject.hide(first))
        subject.claim(pending)
        subject.settle(pending, deleted = false)

        assertEquals(expected = listOf("1", "2"), actual = subject.visible.map { it.id })
    }

    @Test
    fun `claimAll claims every hidden entry`() {
        val subject = deletions()
        subject.setLoaded(listOf(first, second))

        val pending = assertNotNull(subject.hide(first))
        subject.hide(second)

        assertEquals(expected = 2, actual = subject.claimAll().size)
        assertFalse(subject.undo(pending))
        assertTrue(subject.visible.isEmpty())
    }

    @Test
    fun `claimAll returns nothing when no entry is hidden`() {
        val subject = deletions()
        subject.setLoaded(listOf(first))

        assertTrue(subject.claimAll().isEmpty())
    }

    @Test
    fun `hiding a row that is already hidden is refused`() {
        val subject = deletions()
        subject.setLoaded(listOf(first))
        subject.hide(first)

        assertNull(subject.hide(first))
    }

    @Test
    fun `hiding a row that is no longer loaded is refused`() {
        val subject = deletions()
        subject.setLoaded(listOf(second))

        assertNull(subject.hide(first))
    }

    @Test
    fun `hiding a row rebuilt by a later read is accepted`() {
        val subject = deletions()
        subject.setLoaded(listOf(Row("1")))

        assertNotNull(subject.hide(first))
        assertTrue(subject.visible.isEmpty())
    }
}
