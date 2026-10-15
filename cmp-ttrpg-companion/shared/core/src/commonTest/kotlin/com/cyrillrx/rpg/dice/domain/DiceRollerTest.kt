package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DiceRollerTest {
    @Test
    fun `a normal roll draws one result and keeps it`() {
        val roll = DiceRoller(FixedRandom(4)).roll(Die.D6)

        assertEquals(DieRoll(Die.D6, listOf(4), keptIndex = 0), roll)
    }

    @Test
    fun `advantage draws twice and keeps the higher result`() {
        val roll = DiceRoller(FixedRandom(3, 11)).roll(Die.D12, RollMode.ADVANTAGE)

        assertEquals(DieRoll(Die.D12, listOf(3, 11), keptIndex = 1), roll)
    }

    @Test
    fun `disadvantage draws twice and keeps the lower result`() {
        val roll = DiceRoller(FixedRandom(3, 11)).roll(Die.D12, RollMode.DISADVANTAGE)

        assertEquals(DieRoll(Die.D12, listOf(3, 11), keptIndex = 0), roll)
    }

    @Test
    fun `a tie keeps the first result`() {
        val roll = DiceRoller(FixedRandom(7, 7)).roll(Die.D8, RollMode.ADVANTAGE)

        assertEquals(0, roll.keptIndex)
    }

    @Test
    fun `every die rolls between one and its number of sides`() {
        val roller = DiceRoller()

        Die.entries.forEach { die ->
            val results = List(SAMPLE_SIZE) { roller.roll(die).kept }
            assertTrue(results.all { it in 1..die.sides }, "$die rolled outside 1..${die.sides}")
        }
    }

    private companion object {
        const val SAMPLE_SIZE = 1_000
    }
}
