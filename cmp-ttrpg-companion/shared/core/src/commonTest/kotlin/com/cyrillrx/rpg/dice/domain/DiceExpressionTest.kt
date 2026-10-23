package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Result
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
    fun `equal expressions print the same whatever the order they were written in`() {
        assertEquals((1.d20 + 1.d4).toNotation(), (1.d4 + 1.d20).toNotation())
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
    fun `as many dice as the limit are accepted`() {
        assertEquals(MAX_DICE_COUNT, MAX_DICE_COUNT.d6.terms.single().count)
    }

    @Test
    fun `more dice than the limit in one term are refused`() {
        assertFailsWith<IllegalArgumentException> { (MAX_DICE_COUNT + 1).d6 }
    }

    @Test
    fun `more dice than the limit across terms are refused`() {
        assertFailsWith<IllegalArgumentException> { MAX_DICE_COUNT.d6 + 1.d8 }
    }

    @Test
    fun `a modifier up to the limit is accepted on either side`() {
        assertEquals(MAX_MODIFIER, (1.d6 + MAX_MODIFIER).modifier)
        assertEquals(-MAX_MODIFIER, (1.d6 - MAX_MODIFIER).modifier)
    }

    @Test
    fun `a modifier beyond the limit is refused`() {
        assertFailsWith<IllegalArgumentException> { 1.d6 + MAX_MODIFIER + 1 }
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

    @Test
    fun `combining expressions groups their dice and sums their modifiers`() {
        assertEquals(Result.Success(3.d6 + 1.d4 + 3), (2.d6 + 1).combine(1.d6 + 1.d4 + 2))
    }

    @Test
    fun `combining up to the dice limit is accepted`() {
        assertEquals(Result.Success(MAX_DICE_COUNT.d6), (MAX_DICE_COUNT - 1).d6.combine(1.d6))
    }

    @Test
    fun `combining past the dice limit tells the dice limit`() {
        val expected = Result.Failure(DiceNotationError.TooManyDice(MAX_DICE_COUNT))

        assertEquals(expected, MAX_DICE_COUNT.d6.combine(1.d4))
    }

    @Test
    fun `combining past the modifier limit tells the modifier limit`() {
        val expected = Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER))

        assertEquals(expected, (1.d6 + MAX_MODIFIER).combine(1.d6 + 1))
    }

    @Test
    fun `a bonus adds to the modifier`() {
        assertEquals(Result.Success(1.d20 + 5), (1.d20 + 2).withBonus(3))
    }

    @Test
    fun `a negative bonus removes from the modifier`() {
        assertEquals(Result.Success(1.d20 - 1), (1.d20 + 2).withBonus(-3))
    }

    @Test
    fun `a bonus past the modifier limit tells the modifier limit`() {
        val expected = Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER))

        assertEquals(expected, (1.d20 + MAX_MODIFIER).withBonus(1))
    }

    @Test
    fun `a bonus that overflows an int is refused`() {
        val expected = Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER))

        assertEquals(expected, (1.d20 + MAX_MODIFIER).withBonus(Int.MAX_VALUE))
    }
}
