package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import com.cyrillrx.rpg.core.presentation.viewmodel.SEARCH_DEBOUNCE_MS
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemFilter
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListState
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
class MagicalItemListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = SampleMagicalItemRepository()
    private val item = SampleMagicalItemRepository.getFirst()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads all items`() = runTest(testDispatcher) {
        val viewModel = MagicalItemListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<MagicalItemListState.Body.WithData>(state.body)
        assertEquals(expected = 8, actual = body.searchResults.size)
    }

    @Test
    fun `onTypeToggled filters items by type`() = runTest(testDispatcher) {
        val viewModel = MagicalItemListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onTypeToggled(MagicalItem.Type.POTION)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.filter.types.contains(MagicalItem.Type.POTION))
        val body = assertIs<MagicalItemListState.Body.WithData>(state.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
    }

    @Test
    fun `onRarityToggled filters items by rarity`() = runTest(testDispatcher) {
        val viewModel = MagicalItemListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onRarityToggled(MagicalItem.Rarity.RARE)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.filter.rarities.contains(MagicalItem.Rarity.RARE))
        val body = assertIs<MagicalItemListState.Body.WithData>(state.body)
        assertEquals(expected = 3, actual = body.searchResults.size)
    }

    @Test
    fun `filterByQuery filters items by title`() = runTest(testDispatcher) {
        val viewModel = MagicalItemListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val itemName = item.resolveTranslation("en").name
        viewModel.filterByQuery(itemName)

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<MagicalItemListState.Body.WithData>(state.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
        assertEquals(expected = itemName, actual = body.searchResults.first().resolveTranslation("en").name)
    }

    @Test
    fun `onResetFilters clears active filters`() = runTest(testDispatcher) {
        val viewModel = MagicalItemListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onTypeToggled(MagicalItem.Type.POTION)

        advanceUntilIdle()

        assertTrue(viewModel.state.value.filter.hasActiveFilters)

        viewModel.onResetFilters()

        advanceUntilIdle()

        assertFalse(viewModel.state.value.filter.hasActiveFilters)
        assertEquals(expected = MagicalItemFilter(), actual = viewModel.state.value.filter)
        val body = assertIs<MagicalItemListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 8, actual = body.searchResults.size)
    }

    @Test
    fun `state is Empty when no items match filter`() = runTest(testDispatcher) {
        val viewModel = MagicalItemListViewModel(repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.filterByQuery("no_match")

        advanceUntilIdle()

        assertIs<MagicalItemListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `filterByQuery debounces rapid input into a single load`() = runTest(testDispatcher) {
        val countingRepository = CountingMagicalItemRepository()
        val viewModel = MagicalItemListViewModel(countingRepository)

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

        assertEquals(expected = loadsAfterInit + 1, actual = countingRepository.getAllCount)
        assertEquals(expected = "abc", actual = viewModel.state.value.filter.query)
    }

    @Test
    fun `filterByQuery does not flash Loading while data is shown`() = runTest(testDispatcher) {
        val viewModel = MagicalItemListViewModel(repository)
        val bodies = mutableListOf<MagicalItemListState.Body>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { bodies.add(it.body) }
        }

        advanceUntilIdle()
        bodies.clear()

        viewModel.filterByQuery(item.resolveTranslation("en").name)
        advanceUntilIdle()

        assertFalse(bodies.any { it is MagicalItemListState.Body.Loading })
        assertIs<MagicalItemListState.Body.WithData>(viewModel.state.value.body)
    }

    @Test
    fun `filterByQuery does not flash Loading after an empty result`() = runTest(testDispatcher) {
        val viewModel = MagicalItemListViewModel(repository)
        val bodies = mutableListOf<MagicalItemListState.Body>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { bodies.add(it.body) }
        }

        advanceUntilIdle()
        viewModel.filterByQuery("no_match")
        advanceUntilIdle()
        assertIs<MagicalItemListState.Body.Empty>(viewModel.state.value.body)
        bodies.clear()

        viewModel.filterByQuery("still_no_match")
        advanceUntilIdle()

        assertFalse(bodies.any { it is MagicalItemListState.Body.Loading })
        assertIs<MagicalItemListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val failingRepository = FailingMagicalItemRepository()
        val viewModel = MagicalItemListViewModel(failingRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<MagicalItemListState.Body.Error>(viewModel.state.value.body)
    }
}

private class FailingMagicalItemRepository : MagicalItemRepository {
    override suspend fun getAll(filter: MagicalItemFilter?): List<MagicalItem> {
        throw RuntimeException("Simulated repository error")
    }

    override suspend fun getById(id: String): MagicalItem? {
        throw RuntimeException("Simulated repository error")
    }
}

private class CountingMagicalItemRepository : MagicalItemRepository {
    private val delegate = SampleMagicalItemRepository()
    var getAllCount = 0
        private set

    override suspend fun getAll(filter: MagicalItemFilter?): List<MagicalItem> {
        getAllCount++
        return delegate.getAll(filter)
    }

    override suspend fun getById(id: String): MagicalItem? = delegate.getById(id)
}
