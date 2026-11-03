package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModelStore
import com.cyrillrx.rpg.core.presentation.viewmodel.hold
import com.cyrillrx.rpg.dice.data.RamRollHistoryRepository
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class DiceHistoryViewModelTest {

    private val history = RamRollHistoryRepository(clock = FixedClock)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `an empty history reads as empty`() {
        assertEquals(DiceHistoryState.Body.Empty, newViewModel().state.value.body)
    }

    @Test
    fun `a history that fails to load reads as an error`() {
        val viewModel = DiceHistoryViewModel(FailingToLoadRepository(), clock = FixedClock, timeZone = TimeZone.UTC)

        assertEquals(DiceHistoryState.Body.Error, viewModel.state.value.body)
    }

    @Test
    fun `the history shows its rolls newest first under the day they were made`() = runTest {
        recordD4(value = 1)
        recordD4(value = 2)

        val state = assertIs<DiceHistoryState.Body.WithData>(newViewModel().state.value.body)

        assertEquals(LocalDate(2023, 11, 14), state.today)
        assertEquals(listOf(LocalDate(2023, 11, 14)), state.days.map { it.date })
        assertEquals(listOf(2, 1), state.days.single().rolls.map { it.pool.total() })
    }

    @Test
    fun `a roll made while the history is open shows up`() = runTest {
        val viewModel = newViewModel()

        recordD4(value = 3)

        assertEquals(listOf(3), shownTotals(viewModel))
    }

    @Test
    fun `clearing hides every roll until the clear is undone`() = runTest {
        recordD4(value = 1)
        val viewModel = newViewModel()

        val clear = viewModel.clearOptimistically()!!
        assertEquals(DiceHistoryState.Body.Empty, viewModel.state.value.body)

        viewModel.undoClear(clear)
        assertEquals(listOf(1), shownTotals(viewModel))
    }

    @Test
    fun `an empty history has nothing to clear`() {
        assertNull(newViewModel().clearOptimistically())
    }

    @Test
    fun `committing a clear forgets the rolls`() = runTest {
        recordD4(value = 1)
        val viewModel = newViewModel()

        viewModel.commitClear(viewModel.clearOptimistically()!!)

        assertEquals(emptyList(), history.rolls.value)
        assertEquals(DiceHistoryState.Body.Empty, viewModel.state.value.body)
    }

    @Test
    fun `a roll made while a clear can be undone survives the clear`() = runTest {
        recordD4(value = 1)
        val viewModel = newViewModel()

        val clear = viewModel.clearOptimistically()!!
        recordD4(value = 4)
        viewModel.commitClear(clear)

        assertEquals(listOf(4), history.rolls.value.map { it.pool.total() })
        assertEquals(listOf(4), shownTotals(viewModel))
    }

    @Test
    fun `an undone clear is not committed`() = runTest {
        recordD4(value = 1)
        val viewModel = newViewModel()

        val clear = viewModel.clearOptimistically()!!
        viewModel.undoClear(clear)
        viewModel.commitClear(clear)

        assertEquals(listOf(1), history.rolls.value.map { it.pool.total() })
    }

    @Test
    fun `leaving the history commits the clear still pending`() = runTest {
        recordD4(value = 1)
        val store = ViewModelStore()
        val viewModel = store.hold(DiceHistoryViewModel::class) { newViewModel() }

        viewModel.clearOptimistically()
        store.clear()

        assertEquals(emptyList<RecordedRoll>(), history.rolls.value)
    }

    private suspend fun recordD4(value: Int) {
        history.record(RollPool(listOf(SimpleRoll(Die.D4, value))))
    }

    private fun shownTotals(viewModel: DiceHistoryViewModel): List<Int> =
        assertIs<DiceHistoryState.Body.WithData>(viewModel.state.value.body).days.flatMap { day ->
            day.rolls.map { it.pool.total() }
        }

    private fun newViewModel() = DiceHistoryViewModel(history, clock = FixedClock, timeZone = TimeZone.UTC)

    private class FailingToLoadRepository : RollHistoryRepository by RamRollHistoryRepository() {
        override suspend fun load() = throw IllegalStateException("no such column")
    }

    private object FixedClock : Clock {
        override fun now(): Instant = Instant.parse("2023-11-14T22:13:20Z")
    }
}
