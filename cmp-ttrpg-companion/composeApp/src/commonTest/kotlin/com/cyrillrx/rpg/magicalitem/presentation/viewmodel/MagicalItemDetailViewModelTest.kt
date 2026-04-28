package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
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
class MagicalItemDetailViewModelTest {

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
    fun `state is Loading initially`() = runTest(testDispatcher) {
        val viewModel = MagicalItemDetailViewModel(item.id, repository)

        assertEquals(expected = DetailState.Loading, actual = viewModel.state.value)
    }

    @Test
    fun `state is NotFound if item not found`() = runTest(testDispatcher) {
        val viewModel = MagicalItemDetailViewModel("no_match", repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<DetailState.NotFound>(state)
        assertEquals(expected = DetailState.NotFound("no_match"), actual = state)
    }

    @Test
    fun `state emits Found for valid item id`() = runTest(testDispatcher) {
        val viewModel = MagicalItemDetailViewModel(item.id, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<DetailState.Found<MagicalItem>>(state)
        assertEquals(expected = item.id, actual = state.item.id)
        assertEquals(expected = item.translations, actual = state.item.translations)
    }
}
