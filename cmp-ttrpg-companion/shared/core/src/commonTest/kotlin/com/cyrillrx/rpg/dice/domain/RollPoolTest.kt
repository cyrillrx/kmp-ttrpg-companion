package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RollPoolTest {
    private val d20 = DieRoll(Die.D20, listOf(14), keptIndex = 0)
    private val d6 = DieRoll(Die.D6, listOf(3), keptIndex = 0)

    @Test
    fun `an empty pool totals its modifier`() {
        assertEquals(2, RollPool(modifier = 2).total)
    }

    @Test
    fun `the total adds every kept result and the modifier`() {
        val pool = RollPool().add(d20).add(d6).withModifier(-1)

        assertEquals(16, pool.total)
    }

    @Test
    fun `only the kept result of a die counts toward the total`() {
        val withAdvantage = DieRoll(Die.D20, listOf(5, 18), keptIndex = 1)

        assertEquals(18, RollPool().add(withAdvantage).total)
    }

    @Test
    fun `removing a roll drops only that roll`() {
        val pool = RollPool().add(d20).add(d6).remove(0)

        assertEquals(listOf(d6), pool.rolls)
    }

    @Test
    fun `removing a roll that does not exist fails`() {
        assertFailsWith<IllegalArgumentException> { RollPool().add(d6).remove(1) }
    }

    @Test
    fun `a pool with only a modifier is empty`() {
        assertTrue(RollPool(modifier = 3).isEmpty)
    }

    @Test
    fun `a pool with a roll is not empty`() {
        assertFalse(RollPool().add(d6).isEmpty)
    }
}
