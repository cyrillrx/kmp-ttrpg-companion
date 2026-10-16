package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DieTest {
    @Test
    fun `a roll gives the die and the value drawn`() {
        assertEquals(SimpleRoll(Die.D6, 4), Die.D6.roll(FixedRandom(4)))
    }

    @Test
    fun `every die rolls between one and its number of sides`() {
        Die.entries.forEach { die ->
            val values = List(SAMPLE_SIZE) { die.roll().value }
            assertTrue(values.all { it in 1..die.sides }, "$die rolled outside 1..${die.sides}")
        }
    }

    private companion object {
        const val SAMPLE_SIZE = 1_000
    }
}
