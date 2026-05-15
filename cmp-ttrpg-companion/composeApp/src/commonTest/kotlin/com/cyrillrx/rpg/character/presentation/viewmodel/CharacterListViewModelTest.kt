package com.cyrillrx.rpg.character.presentation.viewmodel

import com.cyrillrx.rpg.character.data.RamCharacterRepository
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.CharacterListState
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = RamCharacterRepository()
    private val router = NoOpCharacterRouter()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(
        repo: CharacterRepository = repository,
        router: CharacterRouter = this.router,
    ) = CharacterListViewModel(router, repo)

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<CharacterListState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(FailingCharacterRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CharacterListState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `initial state is Empty when no characters exist`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CharacterListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `initial state is WithData when characters exist`() = runTest(testDispatcher) {
        val character = SampleCharacterRepository.getAll().first()
        repository.save(character)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<CharacterListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
    }

    @Test
    fun `silentRefresh updates state with fresh data without showing Loading`() = runTest(testDispatcher) {
        val character = SampleCharacterRepository.getAll().first()
        repository.save(character)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val newCharacter = SampleCharacterRepository.getAll().last()
        repository.save(newCharacter)

        val emittedBodies = mutableListOf<CharacterListState.Body>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { emittedBodies.add(it.body) }
        }

        viewModel.silentRefresh()
        advanceUntilIdle()

        assertTrue(emittedBodies.none { it is CharacterListState.Body.Loading })
        val body = assertIs<CharacterListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 2, actual = body.searchResults.size)
    }

    @Test
    fun `silentRefresh does nothing when state is already Loading`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<CharacterListState.Body.Loading>(viewModel.state.value.body)

        viewModel.silentRefresh()

        assertIs<CharacterListState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `onSearchQueryChanged updates searchQuery in state`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.onSearchQueryChanged("fire")
        advanceUntilIdle()

        assertEquals(expected = "fire", actual = viewModel.state.value.searchQuery)
    }

    @Test
    fun `openCharacterDetail delegates to router`() = runTest(testDispatcher) {
        val trackingRouter = TrackingCharacterRouter()
        val character = SampleCharacterRepository.getAll().first()
        repository.save(character)

        val viewModel = buildViewModel(router = trackingRouter)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.openCharacterDetail(character)

        assertTrue(trackingRouter.openedCharacters.contains(character))
    }
}

private class NoOpCharacterRouter : CharacterRouter {
    override fun navigateUp() = Unit
    override fun openCharacterDetail(character: Character) = Unit
    override fun openCreateCharacter() = Unit
    override fun openPresetGallery() = Unit
}

private class TrackingCharacterRouter : CharacterRouter {
    val openedCharacters = mutableListOf<Character>()

    override fun navigateUp() = Unit
    override fun openCharacterDetail(character: Character) { openedCharacters.add(character) }
    override fun openCreateCharacter() = Unit
    override fun openPresetGallery() = Unit
}

private class FailingCharacterRepository : CharacterRepository {
    override suspend fun getAll(filter: CharacterFilter?): List<Character> = error("Repository error")
    override suspend fun get(id: String): Character? = error("Repository error")
    override suspend fun getByIds(ids: List<String>): List<Character> = error("Repository error")
    override suspend fun save(character: Character) = error("Repository error")
    override suspend fun delete(id: String) = error("Repository error")
}
