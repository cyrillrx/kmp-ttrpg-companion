package com.cyrillrx.rpg.creature.presentation.viewmodel

import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.presentation.MonsterListState
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class MonsterListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = SampleMonsterRepository()
    private val goblin = SampleMonsterRepository.goblin()
    private val dragon = SampleMonsterRepository.youngRedDragon()
    private val router = NoOpMonsterRouter()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads all monsters`() = runTest(testDispatcher) {
        val viewModel = MonsterListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<MonsterListState.Body.WithData>(state.body)
        assertEquals(expected = 6, actual = body.searchResults.size)
    }

    @Test
    fun `onTypeToggled filters monsters by type`() = runTest(testDispatcher) {
        val viewModel = MonsterListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onTypeToggled(Monster.Type.DRAGON)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.filter.types.contains(Monster.Type.DRAGON))
        val body = assertIs<MonsterListState.Body.WithData>(state.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
        assertEquals(expected = dragon.name, actual = body.searchResults.first().name)
    }

    @Test
    fun `onChallengeRatingToggled filters monsters by CR`() = runTest(testDispatcher) {
        val viewModel = MonsterListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onChallengeRatingToggled(0.25f)

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<MonsterListState.Body.WithData>(state.body)
        assertEquals(expected = 2, actual = body.searchResults.size)
    }

    @Test
    fun `onSearchQueryChanged filters monsters by name`() = runTest(testDispatcher) {
        val viewModel = MonsterListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onSearchQueryChanged(goblin.name)

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<MonsterListState.Body.WithData>(state.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
        assertEquals(expected = goblin.name, actual = body.searchResults.first().name)
    }

    @Test
    fun `onResetFilters clears active filters`() = runTest(testDispatcher) {
        val viewModel = MonsterListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onTypeToggled(Monster.Type.DRAGON)

        advanceUntilIdle()

        assertTrue(viewModel.state.value.filter.hasActiveFilters)

        viewModel.onResetFilters()

        advanceUntilIdle()

        assertFalse(viewModel.state.value.filter.hasActiveFilters)
        assertEquals(expected = MonsterFilter(), actual = viewModel.state.value.filter)
        val body = assertIs<MonsterListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 6, actual = body.searchResults.size)
    }

    @Test
    fun `state is Empty when no monsters match filter`() = runTest(testDispatcher) {
        val viewModel = MonsterListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onSearchQueryChanged("no_match")

        advanceUntilIdle()

        assertIs<MonsterListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val failingRepository = FailingMonsterRepository()
        val viewModel = MonsterListViewModel(router, failingRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<MonsterListState.Body.Error>(viewModel.state.value.body)
    }
}

private class NoOpMonsterRouter : MonsterRouter {
    override fun navigateUp() = Unit
    override fun openCompendium() = Unit
    override fun openDetail(monsterId: String) = Unit
}

private class FailingMonsterRepository : MonsterRepository {
    override suspend fun getAll(filter: MonsterFilter?): List<Monster> {
        throw RuntimeException("Simulated repository error")
    }

    override suspend fun getById(id: String): Monster? {
        throw RuntimeException("Simulated repository error")
    }
}
