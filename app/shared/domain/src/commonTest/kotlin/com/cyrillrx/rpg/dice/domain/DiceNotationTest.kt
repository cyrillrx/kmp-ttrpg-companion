package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class DiceNotationTest {
    @Test
    fun `a notation reads into the expression it describes`() {
        assertEquals(Result.Success(2.d6 + 1.d4 + 3), DiceExpression.parse("2d6+1d4+3"))
    }

    @Test
    fun `spaces and an uppercase D are accepted`() {
        assertEquals(Result.Success(1.d20 + 5), DiceExpression.parse(" 1D20 + 5 "))
    }

    @Test
    fun `non-breaking spaces around a sign are accepted`() {
        assertEquals(Result.Success(2.d6 - 3), DiceExpression.parse("2d6 - 3"))
    }

    @Test
    fun `a refused part is named without its spaces`() {
        assertEquals(Result.Failure(DiceNotationError.SubtractedDice("-1d4")), DiceExpression.parse("2d6 - 1d4"))
    }

    @Test
    fun `leading zeros are accepted on both sides of the d`() {
        assertEquals(Result.Success(1.d6), DiceExpression.parse("01d6"))
        assertEquals(Result.Success(1.d6), DiceExpression.parse("1d06"))
    }

    @Test
    fun `a die without a count is rolled once`() {
        assertEquals(Result.Success(1.d20), DiceExpression.parse("d20"))
    }

    @Test
    fun `constants can be subtracted and come first`() {
        assertEquals(Result.Success(2.d8 + 1), DiceExpression.parse("3+2d8-2"))
    }

    @Test
    fun `an expression writes as a notation from the biggest die`() {
        assertEquals("2d6 + 1d4 + 3", (1.d4 + 2.d6 + 3).toNotation())
    }

    @Test
    fun `a negative modifier writes with its sign and a zero one is left out`() {
        assertEquals("1d20 - 1", (1.d20 - 1).toNotation())
        assertEquals("3d6", 3.d6.toNotation())
    }

    @Test
    fun `an expression read back from its notation is the same expression`() {
        val expression = 1.d4 + 1.d100 + 19.d12 - 4

        assertEquals(Result.Success(expression), DiceExpression.parse(expression.toNotation()))
    }

    @Test
    fun `every sample monster hit dice reads as a notation`() {
        SampleMonsterRepository.getAll().forEach { monster ->
            val parsed = assertIs<Result.Success<DiceExpression>>(DiceExpression.parse(monster.hitDice), monster.id)
            assertEquals(monster.hitDice, parsed.value.toNotation().filterNot(Char::isWhitespace))
        }
    }

    @Test
    fun `as many dice as the limit are accepted`() {
        assertEquals(Result.Success(MAX_DICE_COUNT.d6), DiceExpression.parse("${MAX_DICE_COUNT}d6"))
    }

    @Test
    fun `a space inside a term is refused rather than joined`() {
        assertEquals(Result.Failure(DiceNotationError.Unreadable("1d10 0")), DiceExpression.parse("1d10 0"))
    }

    @Test
    fun `a blank notation is refused`() {
        assertEquals(Result.Failure(DiceNotationError.Blank), DiceExpression.parse("  "))
    }

    @Test
    fun `a sign without a term after it is refused`() {
        assertEquals(Result.Failure(DiceNotationError.DanglingSign), DiceExpression.parse("1d6++2"))
    }

    @Test
    fun `zero dice are refused`() {
        assertEquals(Result.Failure(DiceNotationError.ZeroDice("0d6")), DiceExpression.parse("0d6"))
    }

    @Test
    fun `subtracted dice are refused`() {
        assertEquals(Result.Failure(DiceNotationError.SubtractedDice("-1d4")), DiceExpression.parse("2d6-1d4"))
    }

    @Test
    fun `a die the app does not have is refused`() {
        assertEquals(Result.Failure(DiceNotationError.UnsupportedDie("d7")), DiceExpression.parse("2d7"))
    }

    @Test
    fun `a notation with only constants is refused`() {
        assertEquals(Result.Failure(DiceNotationError.NoDice), DiceExpression.parse("5+3"))
    }

    @Test
    fun `more dice than the limit in one term are refused`() {
        assertEquals(
            Result.Failure(DiceNotationError.TooManyDice(MAX_DICE_COUNT)),
            DiceExpression.parse("${MAX_DICE_COUNT + 1}d6"),
        )
    }

    @Test
    fun `more dice than the limit across terms are refused`() {
        assertEquals(
            Result.Failure(DiceNotationError.TooManyDice(MAX_DICE_COUNT)),
            DiceExpression.parse("${MAX_DICE_COUNT}d6+1d8"),
        )
    }

    @Test
    fun `a dice count beyond an Int is refused without overflowing`() {
        assertEquals(
            Result.Failure(DiceNotationError.TooManyDice(MAX_DICE_COUNT)),
            DiceExpression.parse("999999999999d6"),
        )
    }

    @Test
    fun `a modifier at the limit is accepted`() {
        assertEquals(Result.Success(1.d6 - MAX_MODIFIER), DiceExpression.parse("1d6-$MAX_MODIFIER"))
    }

    @Test
    fun `a modifier beyond the limit is refused`() {
        val notation = "1d6+$MAX_MODIFIER+1"

        assertEquals(Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER)), DiceExpression.parse(notation))
    }

    @Test
    fun `a modifier beyond an Int is refused without overflowing`() {
        val notation = "1d6+${Int.MAX_VALUE}+1"

        assertEquals(Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER)), DiceExpression.parse(notation))
    }

    @Test
    fun `constants that would wrap back into range as an Int are refused`() {
        val notation = "1d6+${Int.MAX_VALUE}+${Int.MAX_VALUE}+2"

        assertEquals(Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER)), DiceExpression.parse(notation))
    }

    @Test
    fun `a part that is neither dice nor a number is refused and named`() {
        assertEquals(Result.Failure(DiceNotationError.Unreadable("+fire")), DiceExpression.parse("1d6+fire"))
    }

    @Test
    fun `a double roll reads into a kept term`() {
        val advantage = DiceExpression(listOf(DiceTerm(count = 2, die = Die.D20, keep = KeepRule.HIGHER)), modifier = 5)
        val disadvantage = DiceExpression(listOf(DiceTerm(count = 2, die = Die.D20, keep = KeepRule.LOWER)))

        assertEquals(Result.Success(advantage), DiceExpression.parse("2d20kh1 + 5"))
        assertEquals(Result.Success(disadvantage), DiceExpression.parse("2D20KL1"))
    }

    @Test
    fun `a keep on anything but one of two dice is refused and named`() {
        assertEquals(Result.Failure(DiceNotationError.UnsupportedKeep("2d20kh2")), DiceExpression.parse("2d20kh2"))
        assertEquals(Result.Failure(DiceNotationError.UnsupportedKeep("+4d6kh1")), DiceExpression.parse("1d8+4d6kh1"))
        assertEquals(Result.Failure(DiceNotationError.UnsupportedKeep("d20kl1")), DiceExpression.parse("d20kl1"))
    }

    @Test
    fun `a double roll counts its two dice against the limit`() {
        val expected = Result.Failure(DiceNotationError.TooManyDice(MAX_DICE_COUNT))

        assertEquals(expected, DiceExpression.parse("2d20kh1 + ${MAX_DICE_COUNT - 1}d6"))
    }

    @Test
    fun `a pool notation reads back as the expression that rolls it`() {
        val withDisadvantage =
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 4),
                discarded = SimpleRoll(Die.D20, value = 11),
                rule = KeepRule.LOWER,
            )
        val rolls = listOf(SimpleRoll(Die.D6, value = 2), withDisadvantage, SimpleRoll(Die.D6, value = 5))
        val expression = assertNotNull(RollPool(rolls, modifier = 3).toExpression())

        assertEquals(Result.Success(expression), DiceExpression.parse(expression.toNotation()))
    }
}
