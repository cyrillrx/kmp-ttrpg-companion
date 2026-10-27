package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AdvantageTest {
    @Test
    fun `advantage keeps the higher roll and discards the other`() {
        val roll = Die.D20.rollWithAdvantage(FixedRandom(3, 17))

        val expected =
            DoubleRoll(kept = SimpleRoll(Die.D20, 17), discarded = SimpleRoll(Die.D20, 3), rule = KeepRule.HIGHER)

        assertEquals(expected, roll)
    }

    @Test
    fun `disadvantage keeps the lower roll and discards the other`() {
        val roll = Die.D20.rollWithDisadvantage(FixedRandom(3, 17))

        val expected =
            DoubleRoll(kept = SimpleRoll(Die.D20, 3), discarded = SimpleRoll(Die.D20, 17), rule = KeepRule.LOWER)

        assertEquals(expected, roll)
    }

    // TODO(#332): move the DoubleRoll tests below to a DieRollTest
    @Test
    fun `a double roll counts as its kept roll`() {
        val roll = DoubleRoll(kept = SimpleRoll(Die.D12, 9), discarded = SimpleRoll(Die.D12, 2), rule = KeepRule.HIGHER)

        assertEquals(Die.D12, roll.die)
        assertEquals(9, roll.value)
    }

    @Test
    fun `a double roll of two different dice fails`() {
        assertFailsWith<IllegalArgumentException> {
            DoubleRoll(kept = SimpleRoll(Die.D20, 15), discarded = SimpleRoll(Die.D6, 2), rule = KeepRule.HIGHER)
        }
    }

    @Test
    fun `equal rolls keep the rule they were rolled with`() {
        assertEquals(KeepRule.LOWER, Die.D20.rollWithDisadvantage(FixedRandom(9, 9)).rule)
    }

    @Test
    fun `a double roll that keeps the lower roll against its rule fails`() {
        assertFailsWith<IllegalArgumentException> {
            DoubleRoll(kept = SimpleRoll(Die.D20, 3), discarded = SimpleRoll(Die.D20, 17), rule = KeepRule.HIGHER)
        }
    }

    @Test
    fun `a double roll that keeps the higher roll against its rule fails`() {
        assertFailsWith<IllegalArgumentException> {
            DoubleRoll(kept = SimpleRoll(Die.D20, 17), discarded = SimpleRoll(Die.D20, 3), rule = KeepRule.LOWER)
        }
    }
}
