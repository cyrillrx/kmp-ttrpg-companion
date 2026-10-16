package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AdvantageTest {
    @Test
    fun `advantage keeps the higher roll and discards the other`() {
        val roll = Die.D20.rollWithAdvantage(FixedRandom(3, 17))

        assertEquals(DoubleRoll(kept = SimpleRoll(Die.D20, 17), discarded = SimpleRoll(Die.D20, 3)), roll)
    }

    @Test
    fun `disadvantage keeps the lower roll and discards the other`() {
        val roll = Die.D20.rollWithDisadvantage(FixedRandom(3, 17))

        assertEquals(DoubleRoll(kept = SimpleRoll(Die.D20, 3), discarded = SimpleRoll(Die.D20, 17)), roll)
    }

    @Test
    fun `a double roll counts as its kept roll`() {
        val roll = DoubleRoll(kept = SimpleRoll(Die.D12, 9), discarded = SimpleRoll(Die.D12, 2))

        assertEquals(Die.D12, roll.die)
        assertEquals(9, roll.value)
    }

    @Test
    fun `a double roll of two different dice fails`() {
        assertFailsWith<IllegalArgumentException> {
            DoubleRoll(kept = SimpleRoll(Die.D20, 15), discarded = SimpleRoll(Die.D6, 2))
        }
    }
}
