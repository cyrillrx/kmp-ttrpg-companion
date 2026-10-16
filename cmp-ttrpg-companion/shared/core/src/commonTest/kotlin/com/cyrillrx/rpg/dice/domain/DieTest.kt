package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DieTest {
    @Test
    fun `a roll gives the die and the value drawn`() {
        assertEquals(SimpleRoll(Die.D6, 4), Die.D6.roll(FixedRandom(4)))
    }

    @Test
    fun `every die can roll its highest face`() {
        Die.entries.forEach { die ->
            assertEquals(die.sides, die.roll(FixedRandom(die.sides)).value)
        }
    }

    @Test
    fun `every die can roll a one`() {
        Die.entries.forEach { die ->
            assertEquals(1, die.roll(FixedRandom(1)).value)
        }
    }

    @Test
    fun `a roll outside the faces of its die fails`() {
        assertFailsWith<IllegalArgumentException> { SimpleRoll(Die.D6, 7) }
        assertFailsWith<IllegalArgumentException> { SimpleRoll(Die.D20, 0) }
    }
}
