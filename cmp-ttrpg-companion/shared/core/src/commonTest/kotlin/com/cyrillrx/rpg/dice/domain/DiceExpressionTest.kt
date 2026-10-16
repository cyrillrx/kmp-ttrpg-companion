package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DiceExpressionTest {
    @Test
    fun `a count and a die make a single term`() {
        assertEquals(DiceExpression(listOf(DiceTerm(3, Die.D6))), 3.d6)
    }

    @Test
    fun `every die has its own notation`() {
        val dice = listOf(1.d4, 1.d6, 1.d8, 1.d10, 1.d12, 1.d20, 1.d100).map { it.terms.single().die }

        assertEquals(Die.entries, dice)
    }

    @Test
    fun `adding expressions keeps each term in order and sums the modifiers`() {
        val expression = (2.d6 + 1) + (1.d4 + 2)

        assertEquals(DiceExpression(listOf(DiceTerm(2, Die.D6), DiceTerm(1, Die.D4)), modifier = 3), expression)
    }

    @Test
    fun `a constant adds to the modifier and a subtracted one removes from it`() {
        assertEquals(4, (1.d20 + 7 - 3).modifier)
    }

    @Test
    fun `fewer than one die is refused`() {
        assertFailsWith<IllegalArgumentException> { 0.d6 }
    }

    @Test
    fun `rolling an expression rolls each die of each term and carries the modifier`() {
        val pool = (2.d6 + 1.d4 + 3).roll(FixedRandom(5, 2, 4))

        val expected = RollPool(
            rolls = listOf(SimpleRoll(Die.D6, 5), SimpleRoll(Die.D6, 2), SimpleRoll(Die.D4, 4)),
            modifier = 3,
        )
        assertEquals(expected, pool)
        assertEquals(14, pool.total())
    }
}
