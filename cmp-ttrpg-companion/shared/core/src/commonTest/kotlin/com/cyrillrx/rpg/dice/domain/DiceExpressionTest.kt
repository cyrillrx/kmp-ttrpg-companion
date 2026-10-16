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
    fun `adding expressions sums the modifiers`() {
        assertEquals(3, ((2.d6 + 1) + (1.d4 + 2)).modifier)
    }

    @Test
    fun `dice of the same type are grouped into one term`() {
        assertEquals(3.d6, 2.d6 + 1.d6)
    }

    @Test
    fun `terms are ordered from the biggest die to the smallest`() {
        val expression = 1.d4 + 3.d6 + 1.d20

        assertEquals(listOf(DiceTerm(1, Die.D20), DiceTerm(3, Die.D6), DiceTerm(1, Die.D4)), expression.terms)
    }

    @Test
    fun `building an expression from terms groups and orders them too`() {
        val terms = listOf(DiceTerm(1, Die.D6), DiceTerm(1, Die.D20), DiceTerm(2, Die.D6))

        assertEquals(1.d20 + 3.d6, DiceExpression(terms))
    }

    @Test
    fun `a constant adds to the modifier and a subtracted one removes from it`() {
        assertEquals(4, (1.d20 + 7 - 3).modifier)
    }

    @Test
    fun `a constant can come before the dice`() {
        assertEquals(2.d6 + 5, 5 + 2.d6)
    }

    @Test
    fun `fewer than one die is refused`() {
        assertFailsWith<IllegalArgumentException> { 0.d6 }
    }

    @Test
    fun `an expression without dice is refused`() {
        assertFailsWith<IllegalArgumentException> { DiceExpression(emptyList(), modifier = 3) }
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
