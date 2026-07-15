package com.cyrillrx.rpg.spell.presentation.viewmodel

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.presentation.viewmodel.SEARCH_DEBOUNCE_MS
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SpellListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = SampleSpellRepository()
    private val spell = SampleSpellRepository.getFirst()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads all spells`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<SpellListState.Body.WithData>(state.body)
        assertEquals(expected = 5, actual = body.searchResults.size)
    }

    @Test
    fun `onSchoolToggled filters spells by school`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onSchoolToggled(Spell.School.EVOCATION)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.filter.schools.contains(Spell.School.EVOCATION))
        val body = assertIs<SpellListState.Body.WithData>(state.body)
        assertEquals(expected = 2, actual = body.searchResults.size)
    }

    @Test
    fun `onLevelToggled filters spells by level`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onLevelToggled(3)

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<SpellListState.Body.WithData>(state.body)
        assertEquals(expected = 2, actual = body.searchResults.size)
    }

    @Test
    fun `onClassToggled filters spells by class`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onClassToggled(Character.Class.BARD)

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<SpellListState.Body.WithData>(state.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
    }

    @Test
    fun `filterByQuery filters spells by title`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val spellName = requireNotNull(spell.translations["en"]).name
        viewModel.filterByQuery(spellName)

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<SpellListState.Body.WithData>(state.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
        assertEquals(expected = spellName, actual = requireNotNull(body.searchResults.first().translations["en"]).name)
    }

    @Test
    fun `onResetFilters clears active filters`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onSchoolToggled(Spell.School.EVOCATION)

        advanceUntilIdle()

        assertTrue(viewModel.state.value.filter.hasActiveFilters)

        viewModel.onResetFilters()

        advanceUntilIdle()

        assertFalse(viewModel.state.value.filter.hasActiveFilters)
        assertEquals(expected = SpellFilter(), actual = viewModel.state.value.filter)
        val body = assertIs<SpellListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 5, actual = body.searchResults.size)
    }

    @Test
    fun `state is Empty when no spells match filter`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.filterByQuery("no_match")

        advanceUntilIdle()

        assertIs<SpellListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `filterByQuery debounces rapid input into a single load`() = runTest(testDispatcher) {
        val countingRepository = CountingSpellRepository()
        val viewModel = SpellListViewModel(countingRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        val loadsAfterInit = countingRepository.getAllCount

        viewModel.filterByQuery("a")
        advanceTimeBy(SEARCH_DEBOUNCE_MS / 2)
        viewModel.filterByQuery("ab")
        advanceTimeBy(SEARCH_DEBOUNCE_MS / 2)
        viewModel.filterByQuery("abc")
        advanceUntilIdle()

        // The whole burst triggers a single load, not one per keystroke.
        assertEquals(expected = loadsAfterInit + 1, actual = countingRepository.getAllCount)
        assertEquals(expected = "abc", actual = viewModel.state.value.filter.query)
    }

    @Test
    fun `filterByQuery does not flash Loading while data is shown`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)
        val bodies = mutableListOf<SpellListState.Body>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { bodies.add(it.body) }
        }

        advanceUntilIdle()
        bodies.clear()

        val spellName = requireNotNull(spell.translations["en"]).name
        viewModel.filterByQuery(spellName)
        advanceUntilIdle()

        assertFalse(bodies.any { it is SpellListState.Body.Loading })
        assertIs<SpellListState.Body.WithData>(viewModel.state.value.body)
    }

    @Test
    fun `filterByQuery does not flash Loading after an empty result`() = runTest(testDispatcher) {
        val viewModel = SpellListViewModel(repository)
        val bodies = mutableListOf<SpellListState.Body>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { bodies.add(it.body) }
        }

        advanceUntilIdle()
        viewModel.filterByQuery("no_match")
        advanceUntilIdle()
        assertIs<SpellListState.Body.Empty>(viewModel.state.value.body)
        bodies.clear()

        viewModel.filterByQuery("still_no_match")
        advanceUntilIdle()

        assertFalse(bodies.any { it is SpellListState.Body.Loading })
        assertIs<SpellListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val failingRepository = FailingSpellRepository()
        val viewModel = SpellListViewModel(failingRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<SpellListState.Body.Error>(viewModel.state.value.body)
    }
}

private class FailingSpellRepository : SpellRepository {
    override suspend fun getAll(filter: SpellFilter?): List<Spell> {
        throw RuntimeException("Simulated repository error")
    }

    override suspend fun getById(id: String): Spell? {
        throw RuntimeException("Simulated repository error")
    }
}

private class CountingSpellRepository : SpellRepository {
    private val delegate = SampleSpellRepository()
    var getAllCount = 0
        private set

    override suspend fun getAll(filter: SpellFilter?): List<Spell> {
        getAllCount++
        return delegate.getAll(filter)
    }

    override suspend fun getById(id: String): Spell? = delegate.getById(id)
}
