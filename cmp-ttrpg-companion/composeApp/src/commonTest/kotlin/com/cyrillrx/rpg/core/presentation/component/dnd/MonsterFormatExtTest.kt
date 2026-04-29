package com.cyrillrx.rpg.core.presentation.component.dnd

import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class MonsterFormatExtTest {

    @Test
    fun `formatCRValue returns 0 for zero`() {
        assertEquals(expected = "0", actual = formatCRValue(0f))
    }

    @Test
    fun `formatCRValue returns 1-8 for 0 125`() {
        assertEquals(expected = "1/8", actual = formatCRValue(0.125f))
    }

    @Test
    fun `formatCRValue returns 1-4 for 0 25`() {
        assertEquals(expected = "1/4", actual = formatCRValue(0.25f))
    }

    @Test
    fun `formatCRValue returns 1-2 for 0 5`() {
        assertEquals(expected = "1/2", actual = formatCRValue(0.5f))
    }

    @Test
    fun `formatCRValue returns integer string for whole number`() {
        assertEquals(expected = "10", actual = formatCRValue(10f))
    }

    @Test
    fun `formatCRValue returns decimal string for non-standard float`() {
        assertEquals(expected = "1.5", actual = formatCRValue(1.5f))
    }

    @Test
    fun `toFormattedCR returns CR prefix with formatted value`() {
        val goblin = SampleMonsterRepository.goblin()
        assertEquals(expected = "CR 1/4", actual = goblin.toFormattedCR())
    }
}
