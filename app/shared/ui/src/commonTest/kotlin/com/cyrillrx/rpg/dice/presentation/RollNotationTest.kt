package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RollNotationTest {

    private val disadvantage = DoubleRoll(
        kept = SimpleRoll(Die.D20, value = 4),
        discarded = SimpleRoll(Die.D20, value = 15),
        rule = KeepRule.LOWER,
    )

    @Test
    fun `an empty pool has no notation`() {
        assertNull(RollPool().toPlainNotation())
    }

    @Test
    fun `a pool without a double roll keeps its notation`() {
        val pool = RollPool(listOf(SimpleRoll(Die.D8, value = 6), SimpleRoll(Die.D6, value = 2)), modifier = 3)

        assertEquals("1d8 + 1d6 + 3", pool.toPlainNotation())
    }

    @Test
    fun `a lone double roll reads as its kept die`() {
        assertEquals("1d20", RollPool(listOf(disadvantage)).toPlainNotation())
    }

    @Test
    fun `a lone double roll keeps its modifier`() {
        assertEquals("1d20 + 5", RollPool(listOf(disadvantage), modifier = 5).toPlainNotation())
    }

    @Test
    fun `a double roll among other dice keeps its keep notation`() {
        val pool = RollPool(listOf(disadvantage, SimpleRoll(Die.D6, value = 2)))

        assertEquals("2d20kl1 + 1d6", pool.toPlainNotation())
    }
}
