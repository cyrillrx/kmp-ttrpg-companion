package com.cyrillrx.rpg.creature.presentation.viewmodel

import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
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
class MonsterDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    val repository = SampleMonsterRepository()
    val creature = SampleMonsterRepository.getFirst()

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
        val viewModel = MonsterDetailViewModel(creature.id, repository)

        assertEquals(expected = DetailState.Loading, actual = viewModel.state.value)
    }

    @Test
    fun `state is NotFound if creature not found`() = runTest(testDispatcher) {
        val viewModel = MonsterDetailViewModel("no_match", repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<DetailState.NotFound>(state)
        assertEquals(expected = DetailState.NotFound("no_match"), actual = state)
    }

    @Test
    fun `state emits Found for valid creature id`() = runTest(testDispatcher) {
        val viewModel = MonsterDetailViewModel(creature.id, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<DetailState.Found<Monster>>(state)
        assertEquals(expected = creature.id, actual = state.item.id)
        assertEquals(expected = creature.resolveTranslation("en").name, actual = state.item.resolveTranslation("en").name)
    }
}
