package com.cyrillrx.rpg.creature.presentation.viewmodel

import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureFilter
import com.cyrillrx.rpg.creature.presentation.CreatureListState
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRouter
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
class CreatureListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = SampleCreatureRepository()
    private val goblin = SampleCreatureRepository.goblin()
    private val dragon = SampleCreatureRepository.youngRedDragon()
    private val router = NoOpCreatureRouter()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads all creatures`() = runTest(testDispatcher) {
        val viewModel = CreatureListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<CreatureListState.Body.WithData>(state.body)
        assertEquals(expected = repository.getAll(null).size, actual = body.searchResults.size)
    }

    @Test
    fun `onTypeToggled filters creatures by type`() = runTest(testDispatcher) {
        val viewModel = CreatureListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onTypeToggled(Creature.Type.DRAGON)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.filter.types.contains(Creature.Type.DRAGON))
        val body = assertIs<CreatureListState.Body.WithData>(state.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
        assertEquals(expected = dragon.name, actual = body.searchResults.first().name)
    }

    @Test
    fun `onChallengeRatingToggled filters creatures by CR`() = runTest(testDispatcher) {
        val viewModel = CreatureListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onChallengeRatingToggled(0.25f)

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<CreatureListState.Body.WithData>(state.body)
        assertEquals(expected = 2, actual = body.searchResults.size)
    }

    @Test
    fun `onSearchQueryChanged filters creatures by name`() = runTest(testDispatcher) {
        val viewModel = CreatureListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onSearchQueryChanged(goblin.name)

        advanceUntilIdle()

        val state = viewModel.state.value
        val body = assertIs<CreatureListState.Body.WithData>(state.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
        assertEquals(expected = goblin.name, actual = body.searchResults.first().name)
    }

    @Test
    fun `onResetFilters clears active filters`() = runTest(testDispatcher) {
        val viewModel = CreatureListViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onTypeToggled(Creature.Type.DRAGON)

        advanceUntilIdle()

        assertTrue(viewModel.state.value.filter.hasActiveFilters)

        viewModel.onResetFilters()

        advanceUntilIdle()

        assertFalse(viewModel.state.value.filter.hasActiveFilters)
        assertEquals(expected = CreatureFilter(), actual = viewModel.state.value.filter)
        val body = assertIs<CreatureListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 6, actual = body.searchResults.size)
    }
}

private class NoOpCreatureRouter : CreatureRouter {
    override fun navigateUp() = Unit
    override fun openCreatureDetail(creature: Creature) = Unit
}
