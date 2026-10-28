package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AdvantageTest {
    @Test
    fun `advantage keeps the higher roll and discards the other`() {
        val roll = Die.D20.rollWithAdvantage(FixedRandom(3, 17))

        val expected =
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 17),
                discarded = SimpleRoll(Die.D20, value = 3),
                rule = KeepRule.HIGHER,
            )

        assertEquals(expected, roll)
    }

    @Test
    fun `disadvantage keeps the lower roll and discards the other`() {
        val roll = Die.D20.rollWithDisadvantage(FixedRandom(3, 17))

        val expected =
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 3),
                discarded = SimpleRoll(Die.D20, value = 17),
                rule = KeepRule.LOWER,
            )

        assertEquals(expected, roll)
    }

    @Test
    fun `equal rolls with advantage keep the higher rule`() {
        assertEquals(KeepRule.HIGHER, Die.D20.rollWithAdvantage(FixedRandom(9, 9)).rule)
    }

    @Test
    fun `equal rolls with disadvantage keep the lower rule`() {
        assertEquals(KeepRule.LOWER, Die.D20.rollWithDisadvantage(FixedRandom(9, 9)).rule)
    }
}
