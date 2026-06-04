package com.cyrillrx.rpg.character.presentation.viewmodel

import com.cyrillrx.rpg.character.data.RamCharacterRepository
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.presentation.CharacterPresetGalleryState
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
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterPresetGalleryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val pcPresetRepository = SampleCharacterRepository()
    private val npcPresetRepository = SampleCharacterRepository()
    private val characterRepository = RamCharacterRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel() = CharacterPresetGalleryViewModel(
        pcPresetRepository = pcPresetRepository,
        npcPresetRepository = npcPresetRepository,
        characterRepository = characterRepository,
    )

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<CharacterPresetGalleryState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is WithData after presets are loaded`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<CharacterPresetGalleryState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = SampleCharacterRepository.getAll().size, actual = body.pcPresets.size)
    }

    @Test
    fun `onTabSelected updates selectedTabIndex`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()
        viewModel.onTabSelected(1)
        assertEquals(expected = 1, actual = viewModel.state.value.selectedTabIndex)
    }

    @Test
    fun `onPresetSelected saves character with a new unique id`() = runTest(testDispatcher) {
        val preset = SampleCharacterRepository.humanFighter()
        val viewModel = buildViewModel()

        var navigationEvent: CharacterPresetGalleryViewModel.NavigationEvent? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.navigationEvents.collect { navigationEvent = it }
        }

        viewModel.onPresetSelected(preset)
        advanceUntilIdle()

        val event = assertIs<CharacterPresetGalleryViewModel.NavigationEvent.NavigateToDetail>(navigationEvent)
        assertNotEquals(illegal = preset.id, actual = event.character.id)
        assertEquals(expected = preset.name, actual = event.character.name)
    }

    @Test
    fun `onPresetSelected persists character to repository`() = runTest(testDispatcher) {
        val preset = SampleCharacterRepository.humanFighter()
        val viewModel = buildViewModel()

        viewModel.onPresetSelected(preset)
        advanceUntilIdle()

        val saved = characterRepository.getAll(null)
        assertEquals(expected = 1, actual = saved.size)
        assertEquals(expected = preset.name, actual = saved.first().name)
    }

    @Test
    fun `onPresetSelected emits NavigateToDetail event with the newly created character`() = runTest(testDispatcher) {
        val preset = SampleCharacterRepository.humanFighter()
        val viewModel = buildViewModel()

        var navigationEvent: CharacterPresetGalleryViewModel.NavigationEvent? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.navigationEvents.collect { navigationEvent = it }
        }

        viewModel.onPresetSelected(preset)
        advanceUntilIdle()

        val event = assertIs<CharacterPresetGalleryViewModel.NavigationEvent.NavigateToDetail>(navigationEvent)
        assertNotEquals(illegal = preset.id, actual = event.character.id)
        assertEquals(expected = preset.name, actual = event.character.name)
        assertEquals(expected = characterRepository.getAll(null).first().id, actual = event.character.id)
    }
}
