package com.cyrillrx.rpg.dice.presentation.viewmodel

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.MAX_DICE_COUNT
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.domain.d20
import com.cyrillrx.rpg.dice.domain.d6
import com.cyrillrx.rpg.dice.presentation.DiceRollerState
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class DiceRollerViewModelTest {
    @Test
    fun `the roller opens on an empty composition`() {
        assertEquals(DiceRollerState(), DiceRollerViewModel().state.value)
    }

    @Test
    fun `adding dice composes the roll`() {
        val viewModel = DiceRollerViewModel()

        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D20)

        assertEquals(1.d20 + 2.d6, viewModel.state.value.composition())
    }

    @Test
    fun `removing a die takes one of that die out of the composition`() {
        val viewModel = DiceRollerViewModel()
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)

        viewModel.removeDie(Die.D6)

        assertEquals(1.d6, viewModel.state.value.composition())
    }

    @Test
    fun `removing the last die of a type empties the composition`() {
        val viewModel = DiceRollerViewModel()
        viewModel.addDie(Die.D6)

        viewModel.removeDie(Die.D6)

        assertNull(viewModel.state.value.composition())
    }

    @Test
    fun `the composition stops at the dice limit`() {
        val viewModel = DiceRollerViewModel()

        repeat(MAX_DICE_COUNT + 1) { viewModel.addDie(Die.D6) }

        assertEquals(MAX_DICE_COUNT, viewModel.state.value.diceCount())
    }

    @Test
    fun `the modifier stays within its limits`() {
        val viewModel = DiceRollerViewModel()

        repeat(MAX_MODIFIER + 1) { viewModel.increaseModifier() }
        assertEquals(MAX_MODIFIER, viewModel.state.value.modifier)

        repeat(2 * MAX_MODIFIER + 1) { viewModel.decreaseModifier() }
        assertEquals(-MAX_MODIFIER, viewModel.state.value.modifier)
    }

    @Test
    fun `rolling throws every composed die with the modifier and keeps the composition`() {
        val viewModel = DiceRollerViewModel(FixedRandom(4, 2))
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)
        viewModel.increaseModifier()

        viewModel.roll()

        val state = viewModel.state.value
        assertEquals(RollPool(listOf(SimpleRoll(Die.D6, 4), SimpleRoll(Die.D6, 2)), modifier = 1), state.lastRoll)
        assertEquals(2.d6 + 1, state.composition())
    }

    @Test
    fun `rolling the same result again still yields a new state`() {
        val viewModel = DiceRollerViewModel(FixedRandom(14, 14))
        viewModel.rollD20()
        val firstRoll = viewModel.state.value

        viewModel.rollD20()

        assertNotEquals(firstRoll, viewModel.state.value)
    }

    @Test
    fun `rolling an empty composition does nothing`() {
        val viewModel = DiceRollerViewModel()

        viewModel.roll()

        assertNull(viewModel.state.value.lastRoll)
    }

    @Test
    fun `a direct d20 adds the modifier and leaves the composition alone`() {
        val viewModel = DiceRollerViewModel(FixedRandom(14))
        viewModel.addDie(Die.D6)
        viewModel.increaseModifier()

        viewModel.rollD20()

        val state = viewModel.state.value
        assertEquals(RollPool(listOf(SimpleRoll(Die.D20, 14)), modifier = 1), state.lastRoll)
        assertEquals(1.d6 + 1, state.composition())
    }

    @Test
    fun `a d20 with advantage keeps the higher of two rolls`() {
        val viewModel = DiceRollerViewModel(FixedRandom(3, 17))

        viewModel.rollD20WithAdvantage()

        val expected = DoubleRoll(kept = SimpleRoll(Die.D20, 17), discarded = SimpleRoll(Die.D20, 3))
        assertEquals(RollPool(listOf(expected)), viewModel.state.value.lastRoll)
    }

    @Test
    fun `a d20 with disadvantage keeps the lower of two rolls`() {
        val viewModel = DiceRollerViewModel(FixedRandom(3, 17))

        viewModel.rollD20WithDisadvantage()

        val expected = DoubleRoll(kept = SimpleRoll(Die.D20, 3), discarded = SimpleRoll(Die.D20, 17))
        assertEquals(RollPool(listOf(expected)), viewModel.state.value.lastRoll)
    }

    @Test
    fun `clearing the composition keeps the modifier and the last roll`() {
        val viewModel = DiceRollerViewModel(FixedRandom(5))
        viewModel.addDie(Die.D8)
        viewModel.increaseModifier()
        viewModel.roll()

        viewModel.clearComposition()

        val state = viewModel.state.value
        assertNull(state.composition())
        assertEquals(1, state.modifier)
        assertEquals(RollPool(listOf(SimpleRoll(Die.D8, 5)), modifier = 1), state.lastRoll)
    }

    private class FixedRandom(vararg values: Int) : Random() {
        private val values = ArrayDeque(values.toList())

        override fun nextBits(bitCount: Int): Int = error("FixedRandom only serves nextInt(from, until)")

        override fun nextInt(from: Int, until: Int): Int = values.removeFirst()
    }
}
