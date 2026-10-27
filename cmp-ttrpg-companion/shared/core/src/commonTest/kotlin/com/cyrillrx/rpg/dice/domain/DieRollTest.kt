package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DieRollTest {
    @Test
    fun `a double roll counts as its kept roll`() {
        val roll =
            DoubleRoll(
                kept = SimpleRoll(Die.D12, value = 9),
                discarded = SimpleRoll(Die.D12, value = 2),
                rule = KeepRule.HIGHER,
            )

        assertEquals(Die.D12, roll.die)
        assertEquals(9, roll.value)
    }

    @Test
    fun `a double roll of two different dice fails`() {
        assertFailsWith<IllegalArgumentException> {
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 15),
                discarded = SimpleRoll(Die.D6, value = 2),
                rule = KeepRule.HIGHER,
            )
        }
    }

    @Test
    fun `a double roll that keeps the lower roll against its rule fails`() {
        assertFailsWith<IllegalArgumentException> {
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 3),
                discarded = SimpleRoll(Die.D20, value = 17),
                rule = KeepRule.HIGHER,
            )
        }
    }

    @Test
    fun `a double roll that keeps the higher roll against its rule fails`() {
        assertFailsWith<IllegalArgumentException> {
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 17),
                discarded = SimpleRoll(Die.D20, value = 3),
                rule = KeepRule.LOWER,
            )
        }
    }
}
