package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Result
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RollPoolTest {
    private val d20 = SimpleRoll(Die.D20, 14)
    private val d6 = SimpleRoll(Die.D6, 3)
    private val doubleRollNotation = { roll: DoubleRoll -> "d${roll.die.sides} ${roll.rule}" }

    @Test
    fun `an empty pool totals its modifier`() {
        assertEquals(2, RollPool(modifier = 2).total())
    }

    @Test
    fun `the total adds every roll and the modifier`() {
        val pool = RollPool(listOf(d20, d6), modifier = -1)

        assertEquals(16, pool.total())
    }

    @Test
    fun `only the kept roll of a double roll counts toward the total`() {
        val withAdvantage =
            DoubleRoll(kept = SimpleRoll(Die.D20, 18), discarded = SimpleRoll(Die.D20, 5), KeepRule.HIGHER)

        assertEquals(21, RollPool(listOf(withAdvantage, d6)).total())
    }

    @Test
    fun `removing a roll drops only that roll`() {
        val pool = RollPool(listOf(d20, d6)).remove(0)

        assertEquals(listOf<DieRoll>(d6), pool.rolls)
    }

    @Test
    fun `removing a roll that does not exist fails`() {
        assertFailsWith<IllegalArgumentException> { RollPool(listOf(d6)).remove(1) }
    }

    @Test
    fun `a pool with only a modifier is empty`() {
        assertTrue(RollPool(modifier = 3).isEmpty())
    }

    @Test
    fun `a pool with a roll is not empty`() {
        assertFalse(RollPool(listOf(d6)).isEmpty())
    }

    @Test
    fun `a pool writes the dice it rolled grouped from the biggest with its modifier`() {
        val pool = RollPool(listOf(d6, d20, SimpleRoll(Die.D6, 5)), modifier = 6)

        assertEquals((1.d20 + 2.d6 + 6).toNotation(), pool.toNotation(doubleRollNotation))
    }

    @Test
    fun `a pool writes a double roll apart from the plain rolls`() {
        val withAdvantage =
            DoubleRoll(kept = SimpleRoll(Die.D20, 18), discarded = SimpleRoll(Die.D20, 5), KeepRule.HIGHER)
        val pool = RollPool(listOf(d6, d20, withAdvantage), modifier = -2)

        assertEquals("d20 HIGHER + 1d20 + 1d6 - 2", pool.toNotation(doubleRollNotation))
    }

    @Test
    fun `a pool writes a double roll after the bigger dice`() {
        val withAdvantage = DoubleRoll(kept = SimpleRoll(Die.D6, 5), discarded = SimpleRoll(Die.D6, 2), KeepRule.HIGHER)
        val pool = RollPool(listOf(withAdvantage, d20))

        assertEquals("1d20 + d6 HIGHER", pool.toNotation(doubleRollNotation))
    }

    @Test
    fun `a pool of a lone double roll writes it with its modifier`() {
        val withAdvantage =
            DoubleRoll(kept = SimpleRoll(Die.D20, 18), discarded = SimpleRoll(Die.D20, 5), KeepRule.HIGHER)

        assertEquals("d20 HIGHER + 5", RollPool(listOf(withAdvantage), modifier = 5).toNotation(doubleRollNotation))
    }

    @Test
    fun `a pool groups its rolls by die from the biggest`() {
        val otherD6 = SimpleRoll(Die.D6, 5)
        val pool = RollPool(listOf(d6, d20, otherD6))

        assertEquals(listOf(Die.D20 to listOf<DieRoll>(d20), Die.D6 to listOf(d6, otherD6)), pool.rollsByDie().toList())
    }

    @Test
    fun `a pool without dice has no notation`() {
        assertNull(RollPool(modifier = 3).toNotation(doubleRollNotation))
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

    @Test
    fun `adding a roll keeps the rolls already in the pool`() {
        assertEquals(Result.Success(RollPool(listOf(d20, d6))), RollPool(listOf(d20)).add(d6))
    }

    @Test
    fun `adding a roll to a full pool tells the dice limit`() {
        val full = RollPool(List(MAX_DICE_COUNT) { d6 })

        assertEquals(Result.Failure(DiceNotationError.TooManyDice(MAX_DICE_COUNT)), full.add(d6))
    }

    @Test
    fun `a modifier up to the limit replaces the previous one`() {
        val expected = Result.Success(RollPool(modifier = -MAX_MODIFIER))

        assertEquals(expected, RollPool(modifier = 3).withModifier(-MAX_MODIFIER))
    }

    @Test
    fun `a modifier beyond the limit tells the modifier limit`() {
        val expected = Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER))

        assertEquals(expected, RollPool().withModifier(MAX_MODIFIER + 1))
    }
}
