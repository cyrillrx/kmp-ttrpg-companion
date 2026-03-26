package com.cyrillrx.rpg.creature.presentation.viewmodel

import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature
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
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class CreatureDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    val repository = SampleCreatureRepository()
    val creature = SampleCreatureRepository.getFirst()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state is Loading initially`() = runTest(testDispatcher) {
        val viewModel = CreatureDetailViewModel(creature.id, repository)

        assertEquals(expected = DetailState.Loading, actual = viewModel.state.value)
    }

    @Test
    fun `state is NotFound if creature not found`() = runTest(testDispatcher) {
        val viewModel = CreatureDetailViewModel("dummy", repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<DetailState.NotFound>(state)
        assertEquals(expected = DetailState.NotFound("dummy"), actual = state)
    }

    @Test
    fun `state emits Found for valid creature id`() = runTest(testDispatcher) {
        val viewModel = CreatureDetailViewModel(creature.id, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<DetailState.Found<Creature>>(state)
        assertEquals(expected = creature.id, actual = state.item.id)
        assertEquals(expected = creature.name, actual = state.item.name)
    }
}
