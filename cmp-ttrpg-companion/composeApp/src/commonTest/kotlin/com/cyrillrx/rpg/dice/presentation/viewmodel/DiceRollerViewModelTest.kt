package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModelStore
import com.cyrillrx.rpg.core.presentation.viewmodel.hold
import com.cyrillrx.rpg.dice.data.RamRollHistoryRepository
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.MAX_DICE_COUNT
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollContext
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
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

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the roller opens on an empty composition`() {
        assertEquals(DiceRollerState(), newViewModel().state.value)
    }

    @Test
    fun `adding dice composes the roll`() {
        val viewModel = newViewModel()

        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D20)

        assertEquals(1.d20 + 2.d6, viewModel.state.value.composition())
    }

    @Test
    fun `removing a die takes one of that die out of the composition`() {
        val viewModel = newViewModel()
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)

        viewModel.removeDie(Die.D6)

        assertEquals(1.d6, viewModel.state.value.composition())
    }

    @Test
    fun `removing the last die of a type empties the composition`() {
        val viewModel = newViewModel()
        viewModel.addDie(Die.D6)

        viewModel.removeDie(Die.D6)

        assertNull(viewModel.state.value.composition())
    }

    @Test
    fun `the composition stops at the dice limit`() {
        val viewModel = newViewModel()

        repeat(MAX_DICE_COUNT + 1) { viewModel.addDie(Die.D6) }

        assertEquals(MAX_DICE_COUNT, viewModel.state.value.diceCount())
    }

    @Test
    fun `the modifier stays within its limits`() {
        val viewModel = newViewModel()

        repeat(MAX_MODIFIER + 1) { viewModel.increaseModifier() }
        assertEquals(MAX_MODIFIER, viewModel.state.value.modifier)

        repeat(2 * MAX_MODIFIER + 1) { viewModel.decreaseModifier() }
        assertEquals(-MAX_MODIFIER, viewModel.state.value.modifier)
    }

    @Test
    fun `rolling throws every composed die with the modifier and keeps the composition`() {
        val viewModel = newViewModel(FixedRandom(4, 2))
        viewModel.addDie(Die.D6)
        viewModel.addDie(Die.D6)
        viewModel.increaseModifier()

        viewModel.roll()

        val state = viewModel.state.value
        assertEquals(
            RollPool(listOf(SimpleRoll(Die.D6, value = 4), SimpleRoll(Die.D6, value = 2)), modifier = 1),
            state.lastRoll,
        )
        assertEquals(2.d6 + 1, state.composition())
    }

    @Test
    fun `rolling the same result again still yields a new state`() {
        val viewModel = newViewModel(FixedRandom(14, 14))
        viewModel.rollD20()
        val firstRoll = viewModel.state.value

        viewModel.rollD20()

        assertNotEquals(firstRoll, viewModel.state.value)
    }

    @Test
    fun `rolling an empty composition does nothing`() {
        val viewModel = newViewModel()

        viewModel.roll()

        assertNull(viewModel.state.value.lastRoll)
    }

    @Test
    fun `a direct d20 adds the modifier and leaves the composition alone`() {
        val viewModel = newViewModel(FixedRandom(14))
        viewModel.addDie(Die.D6)
        viewModel.increaseModifier()

        viewModel.rollD20()

        val state = viewModel.state.value
        assertEquals(RollPool(listOf(SimpleRoll(Die.D20, value = 14)), modifier = 1), state.lastRoll)
        assertEquals(1.d6 + 1, state.composition())
    }

    @Test
    fun `a d20 with advantage keeps the higher of two rolls`() {
        val viewModel = newViewModel(FixedRandom(3, 17))

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
        val viewModel = newViewModel(FixedRandom(3, 17))

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
        val viewModel = newViewModel(FixedRandom(5))
        viewModel.addDie(Die.D8)
        viewModel.increaseModifier()
        viewModel.roll()

        viewModel.clearComposition()

        val state = viewModel.state.value
        assertNull(state.composition())
        assertEquals(1, state.modifier)
        assertEquals(RollPool(listOf(SimpleRoll(Die.D8, value = 5)), modifier = 1), state.lastRoll)
    }

    @Test
    fun `every roll is recorded in the history in the order it was made`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        val viewModel = newViewModel(random = FixedRandom(4, 3, 17), historyRepository = history)
        viewModel.addDie(Die.D6)
        viewModel.increaseModifier()

        viewModel.roll()
        viewModel.rollD20WithAdvantage()
        advanceUntilIdle()

        val advantage =
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 17),
                discarded = SimpleRoll(Die.D20, value = 3),
                KeepRule.HIGHER,
            )
        val expected = listOf(
            RollPool(listOf(advantage), modifier = 1),
            RollPool(listOf(SimpleRoll(Die.D6, value = 4)), modifier = 1),
        )
        assertEquals(expected, history.rolls.value.map(RecordedRoll::pool))
    }

    @Test
    fun `rolling an empty composition records nothing`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        val viewModel = newViewModel(historyRepository = history)

        viewModel.roll()
        advanceUntilIdle()

        assertEquals(emptyList(), history.rolls.value)
    }

    @Test
    fun `a roll that fails to be recorded is still shown and the next one is recorded`() = runTest(testDispatcher) {
        val history = FirstRecordFailingRepository()
        val viewModel = newViewModel(random = FixedRandom(8, 12), historyRepository = history)

        viewModel.rollD20()
        viewModel.rollD20()
        advanceUntilIdle()

        val lastRoll = RollPool(listOf(SimpleRoll(Die.D20, value = 12)))
        assertEquals(lastRoll, viewModel.state.value.lastRoll)
        assertEquals(listOf(lastRoll), history.rolls.value.map(RecordedRoll::pool))
    }

    @Test
    fun `a roll made as the roller closes is still recorded`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        val store = ViewModelStore()
        val viewModel = store.hold(DiceRollerViewModel::class) {
            newViewModel(random = FixedRandom(12), historyRepository = history)
        }

        viewModel.rollD20()
        store.clear()
        advanceUntilIdle()

        assertEquals(
            listOf(RollPool(listOf(SimpleRoll(Die.D20, value = 12)))),
            history.rolls.value.map(RecordedRoll::pool),
        )
    }

    @Test
    fun `the rolls already recorded are previewed as the roller opens`() = runTest(testDispatcher) {
        val history = StoredRollsRepository(
            stored = listOf(
                RollPool(listOf(SimpleRoll(Die.D20, value = 5))),
                RollPool(listOf(SimpleRoll(Die.D20, value = 9))),
            ),
        )

        val viewModel = newViewModel(historyRepository = history)
        advanceUntilIdle()

        assertEquals(listOf(9, 5), viewModel.state.value.previousRolls().map { it.pool.total() })
    }

    @Test
    fun `a new roll pushes the one it follows into the preview`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        history.record(RollPool(listOf(SimpleRoll(Die.D20, value = 5))))
        val viewModel = newViewModel(random = FixedRandom(9, 14), historyRepository = history)

        viewModel.rollD20()
        viewModel.rollD20()
        advanceUntilIdle()

        assertEquals(listOf(9, 5), viewModel.state.value.previousRolls().map { it.pool.total() })
    }

    @Test
    fun `clearing the history empties the preview`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        val viewModel = newViewModel(random = FixedRandom(9, 14), historyRepository = history)
        viewModel.rollD20()
        viewModel.rollD20()
        advanceUntilIdle()

        history.clearUpTo(history.rolls.value.first().id)
        advanceUntilIdle()

        assertEquals(emptyList(), viewModel.state.value.previousRolls())
    }

    @Test
    fun `clearing the history takes the roll off the card`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        val viewModel = newViewModel(random = FixedRandom(9), historyRepository = history)
        viewModel.rollD20()
        advanceUntilIdle()

        history.clearUpTo(history.rolls.value.first().id)
        advanceUntilIdle()

        assertNull(viewModel.state.value.lastRoll)
    }

    @Test
    fun `a roll made after the clear stays on the card`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        val viewModel = newViewModel(random = FixedRandom(9, 14), historyRepository = history)
        viewModel.rollD20()
        advanceUntilIdle()
        history.clearUpTo(history.rolls.value.first().id)

        viewModel.rollD20()
        advanceUntilIdle()

        assertEquals(RollPool(listOf(SimpleRoll(Die.D20, value = 14))), viewModel.state.value.lastRoll)
    }

    private fun newViewModel(
        random: Random = Random.Default,
        historyRepository: RollHistoryRepository = RamRollHistoryRepository(),
    ) = DiceRollerViewModel(historyRepository, random)

    private class FirstRecordFailingRepository(
        private val delegate: RamRollHistoryRepository = RamRollHistoryRepository(),
    ) : RollHistoryRepository by delegate {
        private var hasFailed = false

        override suspend fun record(pool: RollPool, context: RollContext?): Long {
            if (!hasFailed) {
                hasFailed = true
                error("Storage unavailable")
            }

            return delegate.record(pool, context)
        }
    }

    private class StoredRollsRepository(
        private val stored: List<RollPool>,
        private val delegate: RamRollHistoryRepository = RamRollHistoryRepository(),
    ) : RollHistoryRepository by delegate {
        override suspend fun load() {
            stored.forEach { delegate.record(it) }
        }
    }

    private class FixedRandom(vararg values: Int) : Random() {
        private val values = ArrayDeque(values.toList())

        override fun nextBits(bitCount: Int): Int = error("FixedRandom only serves nextInt(from, until)")

        override fun nextInt(from: Int, until: Int): Int = values.removeFirst()
    }
}
