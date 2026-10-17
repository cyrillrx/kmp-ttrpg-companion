package com.cyrillrx.rpg.dice.presentation.viewmodel

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.MAX_DICE_COUNT
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class DiceRollerViewModelTest {
    @Test
    fun `the roller opens on an empty pool`() {
        assertEquals(RollPool(), DiceRollerViewModel().pool.value)
    }

    @Test
    fun `rolling a die adds its result to the pool`() {
        val viewModel = DiceRollerViewModel(FixedRandom(14, 3))

        viewModel.roll(Die.D20)
        viewModel.roll(Die.D6)

        assertEquals(RollPool(listOf(SimpleRoll(Die.D20, 14), SimpleRoll(Die.D6, 3))), viewModel.pool.value)
    }

    @Test
    fun `removing a die drops only that die`() {
        val viewModel = DiceRollerViewModel(FixedRandom(14, 3))
        viewModel.roll(Die.D20)
        viewModel.roll(Die.D6)

        viewModel.remove(0)

        assertEquals(RollPool(listOf(SimpleRoll(Die.D6, 3))), viewModel.pool.value)
    }

    @Test
    fun `the modifier goes up and down one step at a time`() {
        val viewModel = DiceRollerViewModel()

        viewModel.increaseModifier()
        viewModel.increaseModifier()
        viewModel.decreaseModifier()

        assertEquals(1, viewModel.pool.value.modifier)
    }

    @Test
    fun `the modifier can go below zero`() {
        val viewModel = DiceRollerViewModel()

        viewModel.decreaseModifier()

        assertEquals(-1, viewModel.pool.value.modifier)
    }

    @Test
    fun `a d20 with advantage keeps the higher of two rolls`() {
        val viewModel = DiceRollerViewModel(FixedRandom(3, 17))

        viewModel.rollD20WithAdvantage()

        val expected = DoubleRoll(kept = SimpleRoll(Die.D20, 17), discarded = SimpleRoll(Die.D20, 3))
        assertEquals(RollPool(listOf(expected)), viewModel.pool.value)
    }

    @Test
    fun `a d20 with disadvantage keeps the lower of two rolls`() {
        val viewModel = DiceRollerViewModel(FixedRandom(3, 17))

        viewModel.rollD20WithDisadvantage()

        val expected = DoubleRoll(kept = SimpleRoll(Die.D20, 3), discarded = SimpleRoll(Die.D20, 17))
        assertEquals(RollPool(listOf(expected)), viewModel.pool.value)
    }

    @Test
    fun `a full pool ignores a d20 with advantage or disadvantage`() {
        val viewModel = DiceRollerViewModel(FixedRandom(*IntArray(MAX_DICE_COUNT + 4) { 1 }))
        repeat(MAX_DICE_COUNT) { viewModel.roll(Die.D6) }

        viewModel.rollD20WithAdvantage()
        viewModel.rollD20WithDisadvantage()

        assertEquals(MAX_DICE_COUNT, viewModel.pool.value.rolls.size)
    }

    @Test
    fun `a full pool ignores another die`() {
        val viewModel = DiceRollerViewModel(FixedRandom(*IntArray(MAX_DICE_COUNT + 1) { 1 }))
        repeat(MAX_DICE_COUNT + 1) { viewModel.roll(Die.D6) }

        assertEquals(MAX_DICE_COUNT, viewModel.pool.value.rolls.size)
    }

    @Test
    fun `the modifier stays within its limit`() {
        val viewModel = DiceRollerViewModel()

        repeat(MAX_MODIFIER + 1) { viewModel.increaseModifier() }

        assertEquals(MAX_MODIFIER, viewModel.pool.value.modifier)
    }

    @Test
    fun `the modifier stays within its negative limit`() {
        val viewModel = DiceRollerViewModel()

        repeat(MAX_MODIFIER + 1) { viewModel.decreaseModifier() }

        assertEquals(-MAX_MODIFIER, viewModel.pool.value.modifier)
    }

    @Test
    fun `resetting clears the dice and the modifier`() {
        val viewModel = DiceRollerViewModel(FixedRandom(5))
        viewModel.roll(Die.D8)
        viewModel.increaseModifier()

        viewModel.reset()

        assertEquals(RollPool(), viewModel.pool.value)
    }

    private class FixedRandom(vararg values: Int) : Random() {
        private val values = ArrayDeque(values.toList())

        override fun nextBits(bitCount: Int): Int = error("FixedRandom only serves nextInt(from, until)")

        override fun nextInt(from: Int, until: Int): Int = values.removeFirst()
    }
}
