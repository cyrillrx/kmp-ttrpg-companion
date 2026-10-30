package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModelStore
import com.cyrillrx.rpg.core.presentation.viewmodel.hold
import com.cyrillrx.rpg.dice.data.RamDiceCompositionRepository
import com.cyrillrx.rpg.dice.domain.DiceComposition
import com.cyrillrx.rpg.dice.domain.DiceCompositionRepository
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.MAX_DICE_COUNT
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.domain.d20
import com.cyrillrx.rpg.dice.domain.d6
import com.cyrillrx.rpg.dice.presentation.DiceRollerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class DiceRollerViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the roller opens on an empty composition`() = runTest {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository())
        advanceUntilIdle()

        assertEquals(DiceRollerState(), viewModel.state.value)
    }

    @Test
    fun `adding dice composes the roll`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository())

        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D20)

        assertEquals(1.d20 + 2.d6, viewModel.state.value.expression())
    }

    @Test
    fun `removing a die takes one of that die out of the composition`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository())
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)

        viewModel.removeDie(Die.D6)

        assertEquals(1.d6, viewModel.state.value.expression())
    }

    @Test
    fun `removing the last die of a type empties the composition`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository())
        viewModel.addDie(Die.D6)

        viewModel.removeDie(Die.D6)

        assertNull(viewModel.state.value.expression())
    }

    @Test
    fun `the composition stops at the dice limit`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository())

        repeat(MAX_DICE_COUNT + 1) { viewModel.addDie(Die.D6) }

        assertEquals(MAX_DICE_COUNT, viewModel.state.value.diceCount())
    }

    @Test
    fun `the modifier stays within its limits`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository())

        repeat(MAX_MODIFIER + 1) { viewModel.increaseModifier() }
        assertEquals(MAX_MODIFIER, viewModel.state.value.modifier)

        repeat(2 * MAX_MODIFIER + 1) { viewModel.decreaseModifier() }
        assertEquals(-MAX_MODIFIER, viewModel.state.value.modifier)
    }

    @Test
    fun `rolling throws every composed die with the modifier and keeps the composition`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository(), FixedRandom(4, 2))
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)
        viewModel.increaseModifier()

        viewModel.roll()

        val state = viewModel.state.value
        assertEquals(
            RollPool(listOf(SimpleRoll(Die.D6, value = 4), SimpleRoll(Die.D6, value = 2)), modifier = 1),
            state.lastRoll,
        )
        assertEquals(2.d6 + 1, state.expression())
    }

    @Test
    fun `rolling the same result again still yields a new state`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository(), FixedRandom(14, 14))
        viewModel.rollD20()
        val firstRoll = viewModel.state.value

        viewModel.rollD20()

        assertNotEquals(firstRoll, viewModel.state.value)
    }

    @Test
    fun `rolling an empty composition does nothing`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository())

        viewModel.roll()

        assertNull(viewModel.state.value.lastRoll)
    }

    @Test
    fun `a direct d20 adds the modifier and leaves the composition alone`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository(), FixedRandom(14))
        viewModel.addDie(Die.D6)
        viewModel.increaseModifier()

        viewModel.rollD20()

        val state = viewModel.state.value
        assertEquals(RollPool(listOf(SimpleRoll(Die.D20, value = 14)), modifier = 1), state.lastRoll)
        assertEquals(1.d6 + 1, state.expression())
    }

    @Test
    fun `a d20 with advantage keeps the higher of two rolls`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository(), FixedRandom(3, 17))

        viewModel.rollD20WithAdvantage()

        val expected =
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 17),
                discarded = SimpleRoll(Die.D20, value = 3),
                rule = KeepRule.HIGHER,
            )
        assertEquals(RollPool(listOf(expected)), viewModel.state.value.lastRoll)
    }

    @Test
    fun `a d20 with disadvantage keeps the lower of two rolls`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository(), FixedRandom(3, 17))

        viewModel.rollD20WithDisadvantage()

        val expected =
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 3),
                discarded = SimpleRoll(Die.D20, value = 17),
                rule = KeepRule.LOWER,
            )
        assertEquals(RollPool(listOf(expected)), viewModel.state.value.lastRoll)
    }

    @Test
    fun `clearing the composition keeps the modifier and the last roll`() {
        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository(), FixedRandom(5))
        viewModel.addDie(Die.D8)
        viewModel.increaseModifier()
        viewModel.roll()

        viewModel.clearComposition()

        val state = viewModel.state.value
        assertNull(state.expression())
        assertEquals(1, state.modifier)
        assertEquals(RollPool(listOf(SimpleRoll(Die.D8, value = 5)), modifier = 1), state.lastRoll)
    }

    @Test
    fun `the roller opens on the composition saved last time`() = runTest {
        val saved = DiceComposition(countByDie = mapOf(Die.D6 to 2), modifier = 3)

        val viewModel = DiceRollerViewModel(RamDiceCompositionRepository(saved))
        advanceUntilIdle()

        assertEquals(2.d6 + 3, viewModel.state.value.expression())
    }

    @Test
    fun `dice added before the saved composition loads are kept and saved`() = runTest {
        val repository = RamDiceCompositionRepository(DiceComposition(countByDie = mapOf(Die.D8 to 1), modifier = 2))
        val viewModel = DiceRollerViewModel(repository)

        viewModel.addDie(Die.D20)
        advanceUntilIdle()

        assertEquals(1.d20, viewModel.state.value.expression())
        assertEquals(DiceComposition(countByDie = mapOf(Die.D20 to 1)), repository.load())
    }

    @Test
    fun `composing a roll saves the dice and the modifier`() = runTest {
        val repository = RamDiceCompositionRepository()
        val viewModel = DiceRollerViewModel(repository)
        advanceUntilIdle()

        viewModel.addDie(Die.D20)
        viewModel.increaseModifier()
        advanceUntilIdle()

        assertEquals(DiceComposition(countByDie = mapOf(Die.D20 to 1), modifier = 1), repository.load())
    }

    @Test
    fun `clearing the composition saves a composition without dice`() = runTest {
        val repository = RamDiceCompositionRepository(DiceComposition(countByDie = mapOf(Die.D8 to 1), modifier = 2))
        val viewModel = DiceRollerViewModel(repository)
        advanceUntilIdle()

        viewModel.clearComposition()
        advanceUntilIdle()

        assertEquals(DiceComposition(modifier = 2), repository.load())
    }

    @Test
    fun `rolling does not save the composition again`() = runTest {
        val repository = RamDiceCompositionRepository()
        val viewModel = DiceRollerViewModel(repository, FixedRandom(4, 14))
        viewModel.addDie(Die.D6)
        advanceUntilIdle()
        val storedElsewhere = DiceComposition(modifier = 7)
        repository.save(storedElsewhere)

        viewModel.roll()
        viewModel.rollD20()
        advanceUntilIdle()

        assertEquals(storedElsewhere, repository.load())
    }

    @Test
    fun `the roller opens empty when the saved composition cannot be read`() = runTest {
        val viewModel = DiceRollerViewModel(FailingCompositionRepository())
        advanceUntilIdle()

        assertEquals(DiceRollerState(), viewModel.state.value)
    }

    @Test
    fun `a composition that fails to save is saved again on the next state change`() = runTest {
        val repository = FirstSaveFailingRepository()
        val viewModel = DiceRollerViewModel(repository, FixedRandom(14))
        advanceUntilIdle()

        viewModel.addDie(Die.D4)
        advanceUntilIdle()
        viewModel.rollD20()
        advanceUntilIdle()

        assertEquals(DiceComposition(countByDie = mapOf(Die.D4 to 1)), repository.load())
    }

    @Test
    fun `a change made just before leaving the roller is still saved`() = runTest {
        val repository = RamDiceCompositionRepository()
        val store = ViewModelStore()
        val viewModel = store.hold(DiceRollerViewModel::class) { DiceRollerViewModel(repository) }
        advanceUntilIdle()

        viewModel.addDie(Die.D12)
        store.clear()
        advanceUntilIdle()

        assertEquals(DiceComposition(countByDie = mapOf(Die.D12 to 1)), repository.load())
    }

    @Test
    fun `a die added and left before the saved composition loads is saved`() = runTest {
        val repository = RamDiceCompositionRepository(DiceComposition(countByDie = mapOf(Die.D6 to 2), modifier = 3))
        val store = ViewModelStore()
        val viewModel = store.hold(DiceRollerViewModel::class) { DiceRollerViewModel(repository) }

        viewModel.addDie(Die.D20)
        store.clear()
        advanceUntilIdle()

        assertEquals(DiceComposition(countByDie = mapOf(Die.D20 to 1)), repository.load())
    }

    @Test
    fun `leaving the roller before the saved composition loads keeps it`() = runTest {
        val saved = DiceComposition(countByDie = mapOf(Die.D6 to 2), modifier = 3)
        val repository = RamDiceCompositionRepository(saved)
        val store = ViewModelStore()
        store.hold(DiceRollerViewModel::class) { DiceRollerViewModel(repository) }

        store.clear()
        advanceUntilIdle()

        assertEquals(saved, repository.load())
    }

    private class FailingCompositionRepository : DiceCompositionRepository {
        override suspend fun load(): DiceComposition = error("Storage unavailable")

        override suspend fun save(composition: DiceComposition) = error("Storage unavailable")
    }

    private class FirstSaveFailingRepository(
        private val delegate: RamDiceCompositionRepository = RamDiceCompositionRepository(),
    ) : DiceCompositionRepository by delegate {
        private var hasFailed = false

        override suspend fun save(composition: DiceComposition) {
            if (!hasFailed) {
                hasFailed = true
                error("Storage unavailable")
            }

            delegate.save(composition)
        }
    }

    private class FixedRandom(vararg values: Int) : Random() {
        private val values = ArrayDeque(values.toList())

        override fun nextBits(bitCount: Int): Int = error("FixedRandom only serves nextInt(from, until)")

        override fun nextInt(from: Int, until: Int): Int = values.removeFirst()
    }
}
