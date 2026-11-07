package com.cyrillrx.rpg.spell.presentation.viewmodel

import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
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
class SpellDetailViewModelTest {

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
    fun `state is Loading initially`() = runTest(testDispatcher) {
        val viewModel = SpellDetailViewModel(spell.id, repository)

        assertEquals(expected = DetailState.Loading, actual = viewModel.state.value)
    }

    @Test
    fun `state is NotFound if spell not found`() = runTest(testDispatcher) {
        val viewModel = SpellDetailViewModel("no_match", repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<DetailState.NotFound>(state)
        assertEquals(expected = DetailState.NotFound("no_match"), actual = state)
    }

    @Test
    fun `state emits Found for valid spell id`() = runTest(testDispatcher) {
        val viewModel = SpellDetailViewModel(spell.id, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<DetailState.Found<Spell>>(state)
        assertEquals(expected = spell.id, actual = state.item.id)
        assertEquals(expected = spell.translations, actual = state.item.translations)
    }
}
