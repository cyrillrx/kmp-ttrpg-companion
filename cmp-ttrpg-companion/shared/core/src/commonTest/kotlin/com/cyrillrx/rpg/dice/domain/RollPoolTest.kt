package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
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

    @Test
    fun `a pool reads as the expression it rolled`() {
        val pool = RollPool(listOf(d6, d20, SimpleRoll(Die.D6, 5)), modifier = 6)

        assertEquals(1.d20 + 2.d6 + 6, pool.toExpression())
    }

    @Test
    fun `a pool groups its rolls by die from the biggest`() {
        val otherD6 = SimpleRoll(Die.D6, 5)
        val pool = RollPool(listOf(d6, d20, otherD6))

        assertEquals(listOf(Die.D20 to listOf<DieRoll>(d20), Die.D6 to listOf(d6, otherD6)), pool.rollsByDie().toList())
    }

    @Test
    fun `a pool without dice has no expression`() {
        assertNull(RollPool(modifier = 3).toExpression())
    }

    @Test
    fun `more dice than the limit are refused`() {
        assertFailsWith<IllegalArgumentException> { RollPool(List(MAX_DICE_COUNT + 1) { d6 }) }
    }

    @Test
    fun `a modifier beyond the limit is refused`() {
        assertFailsWith<IllegalArgumentException> { RollPool(modifier = MAX_MODIFIER + 1) }
        assertFailsWith<IllegalArgumentException> { RollPool(modifier = -MAX_MODIFIER - 1) }
    }
}
