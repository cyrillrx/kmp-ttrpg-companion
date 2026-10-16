package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RollPoolTest {
    private val d20 = SimpleRoll(Die.D20, 14)
    private val d6 = SimpleRoll(Die.D6, 3)

    @Test
    fun `an empty pool totals its modifier`() {
        assertEquals(2, RollPool(modifier = 2).total())
    }

    @Test
    fun `the total adds every roll and the modifier`() {
        val pool = RollPool().add(d20).add(d6).withModifier(-1)

        assertEquals(16, pool.total())
    }

    @Test
    fun `only the kept roll of a double roll counts toward the total`() {
        val withAdvantage = DoubleRoll(kept = SimpleRoll(Die.D20, 18), discarded = SimpleRoll(Die.D20, 5))

        assertEquals(21, RollPool().add(withAdvantage).add(d6).total())
    }

    @Test
    fun `removing a roll drops only that roll`() {
        val pool = RollPool().add(d20).add(d6).remove(0)

        assertEquals(listOf<DieRoll>(d6), pool.rolls)
    }

    @Test
    fun `removing a roll that does not exist fails`() {
        assertFailsWith<IllegalArgumentException> { RollPool().add(d6).remove(1) }
    }

    @Test
    fun `a pool with only a modifier is empty`() {
        assertTrue(RollPool(modifier = 3).isEmpty())
    }

    @Test
    fun `a pool with a roll is not empty`() {
        assertFalse(RollPool().add(d6).isEmpty())
    }
}
