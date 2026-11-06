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
import com.cyrillrx.rpg.dice.presentation.RollDay
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

    private val testDispatcher = StandardTestDispatcher()
    private val history = RamRollHistoryRepository(clock = FixedClock)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the history reads as loading until it is loaded`() = runTest(testDispatcher) {
        assertEquals(DiceHistoryState.Body.Loading, newViewModel().state.value.body)
    }

    @Test
    fun `an empty history reads as empty`() = runTest(testDispatcher) {
        assertEquals(DiceHistoryState.Body.Empty, openHistory().state.value.body)
    }

    @Test
    fun `a history that fails to load reads as an error`() = runTest(testDispatcher) {
        assertEquals(DiceHistoryState.Body.Error, openHistory(FailingToLoadRepository()).state.value.body)
    }

    @Test
    fun `the history shows its rolls newest first under the day they were made`() = runTest(testDispatcher) {
        recordD4(value = 1)
        recordD4(value = 2)

        val state = assertIs<DiceHistoryState.Body.WithData>(openHistory().state.value.body)

        assertEquals(LocalDate(year = 2023, month = 11, day = 14), state.today)
        assertEquals(listOf(LocalDate(year = 2023, month = 11, day = 14)), state.days.map(RollDay::date))
        assertEquals(listOf(2, 1), state.days.single().rolls.map { it.pool.total() })
    }

    @Test
    fun `a roll made while the history is open shows up`() = runTest(testDispatcher) {
        val viewModel = openHistory()

        recordD4(value = 3)
        advanceUntilIdle()

        assertEquals(listOf(3), shownTotals(viewModel))
    }

    @Test
    fun `clearing hides every roll until the clear is undone`() = runTest(testDispatcher) {
        recordD4(value = 1)
        val viewModel = openHistory()

        val clear = viewModel.clearOptimistically()!!
        advanceUntilIdle()
        assertEquals(DiceHistoryState.Body.Empty, viewModel.state.value.body)

        viewModel.undoClear(clear)
        advanceUntilIdle()
        assertEquals(listOf(1), shownTotals(viewModel))
    }

    @Test
    fun `a pending clear tells up to which roll it hides until undone`() = runTest(testDispatcher) {
        recordD4(value = 1)
        val viewModel = openHistory()

        val clear = viewModel.clearOptimistically()!!
        advanceUntilIdle()
        assertEquals(clear, viewModel.state.value.hiddenUpTo)

        viewModel.undoClear(clear)
        advanceUntilIdle()
        assertNull(viewModel.state.value.hiddenUpTo)
    }

    @Test
    fun `a second clear before the history redraws has nothing left to clear`() = runTest(testDispatcher) {
        recordD4(value = 1)
        val viewModel = openHistory()

        viewModel.clearOptimistically()

        assertNull(viewModel.clearOptimistically())
    }

    @Test
    fun `an empty history has nothing to clear`() = runTest(testDispatcher) {
        assertNull(openHistory().clearOptimistically())
    }

    @Test
    fun `committing a clear forgets the rolls`() = runTest(testDispatcher) {
        recordD4(value = 1)
        val viewModel = openHistory()

        viewModel.commitClear(viewModel.clearOptimistically()!!)
        advanceUntilIdle()

        assertEquals(emptyList(), history.rolls.value)
        assertEquals(DiceHistoryState.Body.Empty, viewModel.state.value.body)
    }

    @Test
    fun `a roll made while a clear can be undone survives the clear`() = runTest(testDispatcher) {
        recordD4(value = 1)
        val viewModel = openHistory()

        val clear = viewModel.clearOptimistically()!!
        recordD4(value = 4)
        viewModel.commitClear(clear)
        advanceUntilIdle()

        assertEquals(listOf(4), history.rolls.value.map { it.pool.total() })
        assertEquals(listOf(4), shownTotals(viewModel))
    }

    @Test
    fun `an undone clear is not committed`() = runTest(testDispatcher) {
        recordD4(value = 1)
        val viewModel = openHistory()

        val clear = viewModel.clearOptimistically()!!
        viewModel.undoClear(clear)
        viewModel.commitClear(clear)
        advanceUntilIdle()

        assertEquals(listOf(1), history.rolls.value.map { it.pool.total() })
    }

    @Test
    fun `leaving the history commits the clear still pending`() = runTest(testDispatcher) {
        recordD4(value = 1)
        val store = ViewModelStore()
        val viewModel = store.hold(DiceHistoryViewModel::class) { newViewModel() }
        advanceUntilIdle()

        viewModel.clearOptimistically()
        store.clear()
        advanceUntilIdle()

        assertEquals(emptyList<RecordedRoll>(), history.rolls.value)
    }

    @Test
    fun `a clear that fails shows the rolls again and reports the error`() = runTest(testDispatcher) {
        val repository = FailingToClearRepository()
        repository.record(d4(value = 1))
        val viewModel = openHistory(repository)
        val events = mutableListOf<DiceHistoryViewModel.Event>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.events.toList(events) }

        viewModel.commitClear(viewModel.clearOptimistically()!!)
        advanceUntilIdle()

        assertEquals(listOf(1), shownTotals(viewModel))
        assertEquals(listOf<DiceHistoryViewModel.Event>(DiceHistoryViewModel.Event.ClearError), events)
    }

    @Test
    fun `a clear still running is not committed a second time on leaving`() = runTest(testDispatcher) {
        val repository = SlowToClearRepository()
        repository.record(d4(value = 1))
        val viewModel = openHistory(repository)

        viewModel.commitClear(viewModel.clearOptimistically()!!)
        viewModel.commitPendingClears()
        advanceUntilIdle()
        assertEquals(DiceHistoryState.Body.Empty, viewModel.state.value.body)

        repository.release.complete(Unit)
        advanceUntilIdle()
        assertEquals(1, repository.clearCount)
    }

    private suspend fun recordD4(value: Int) {
        history.record(d4(value))
    }

    private fun d4(value: Int) = RollPool(listOf(SimpleRoll(Die.D4, value)))

    private fun shownTotals(viewModel: DiceHistoryViewModel): List<Int> =
        assertIs<DiceHistoryState.Body.WithData>(viewModel.state.value.body).days.flatMap { day ->
            day.rolls.map { it.pool.total() }
        }

    private fun TestScope.openHistory(repository: RollHistoryRepository = history) =
        newViewModel(repository).also { advanceUntilIdle() }

    private fun newViewModel(repository: RollHistoryRepository = history) =
        DiceHistoryViewModel(repository, clock = FixedClock, timeZone = TimeZone.UTC)

    private class FailingToLoadRepository : RollHistoryRepository by RamRollHistoryRepository() {
        override suspend fun load() = throw IllegalStateException("no such column")
    }

    private class FailingToClearRepository : RollHistoryRepository by RamRollHistoryRepository(FixedClock) {
        override suspend fun clearUpTo(id: Long) = throw IllegalStateException("disk I/O error")
    }

    private class SlowToClearRepository(
        private val delegate: RollHistoryRepository = RamRollHistoryRepository(FixedClock),
    ) : RollHistoryRepository by delegate {
        val release = CompletableDeferred<Unit>()
        var clearCount = 0
            private set

        override suspend fun clearUpTo(id: Long) {
            clearCount++
            release.await()
            delegate.clearUpTo(id)
        }
    }

    private object FixedClock : Clock {
        override fun now(): Instant = Instant.parse("2023-11-14T22:13:20Z")
    }
}
