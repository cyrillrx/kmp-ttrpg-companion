package com.cyrillrx.rpg.character.presentation.viewmodel

import com.cyrillrx.rpg.character.data.RamCharacterRepository
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.CharacterListState
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

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(repo: CharacterRepository = repository) =
        CharacterListViewModel(repo, testDispatcher)

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
    fun `silentRefresh keeps existing state when repository throws`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(FailsOnSecondCallCharacterRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CharacterListState.Body.Empty>(viewModel.state.value.body)

        viewModel.silentRefresh()
        advanceUntilIdle()

        assertIs<CharacterListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `filterByQuery updates searchQuery in state`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.filterByQuery("fire")
        advanceUntilIdle()

        assertEquals(expected = "fire", actual = viewModel.state.value.searchQuery)
    }

    @Test
    fun `deleteCharacterOptimistically removes the character from UI`() = runTest(testDispatcher) {
        val character = SampleCharacterRepository.getAll().first()
        repository.save(character)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.deleteCharacterOptimistically(character)

        assertIs<CharacterListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `undoDeletion restores the character`() = runTest(testDispatcher) {
        val character = SampleCharacterRepository.getAll().first()
        repository.save(character)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val pending = requireNotNull(viewModel.deleteCharacterOptimistically(character))
        viewModel.undoDeletion(pending)

        val restoredBody = assertIs<CharacterListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(character, restoredBody.searchResults.first())
    }

    @Test
    fun `commitDeletion removes the character from repository`() = runTest(testDispatcher) {
        val character = SampleCharacterRepository.getAll().first()
        repository.save(character)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val pending = requireNotNull(viewModel.deleteCharacterOptimistically(character))
        viewModel.commitDeletion(pending)
        advanceUntilIdle()

        assertTrue(repository.getAll(null).isEmpty())
    }

    @Test
    fun `commitDeletion restores character and emits error when repository throws`() = runTest(testDispatcher) {
        val failingRepo = FailsOnDeleteCharacterRepository()
        val character = SampleCharacterRepository.getAll().first()
        failingRepo.save(character)

        val viewModel = buildViewModel(repo = failingRepo)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val receivedEvents = mutableListOf<CharacterListViewModel.Event>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { receivedEvents.add(it) }
        }

        val pending = requireNotNull(viewModel.deleteCharacterOptimistically(character))
        viewModel.commitDeletion(pending)
        advanceUntilIdle()

        val body = assertIs<CharacterListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(character, body.searchResults.first())
        assertEquals(1, receivedEvents.size)
        assertIs<CharacterListViewModel.Event.DeletionError>(receivedEvents.first())
    }

    @Test
    fun `commitAllPendingDeletions commits pending deletions that were never confirmed`() = runTest(testDispatcher) {
        val character = SampleCharacterRepository.getAll().first()
        repository.save(character)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.deleteCharacterOptimistically(character) // no commit
        viewModel.commitAllPendingDeletions()
        advanceUntilIdle()

        assertTrue(repository.getAll(null).isEmpty())
    }
}

private class FailingCharacterRepository : CharacterRepository {
    override suspend fun getAll(filter: CharacterFilter?): List<Character> = error("Repository error")
    override suspend fun get(id: String): Character? = error("Repository error")
    override suspend fun getByIds(ids: List<String>): List<Character> = error("Repository error")
    override suspend fun save(character: Character) = error("Repository error")
    override suspend fun delete(id: String) = error("Repository error")
}

private class FailsOnDeleteCharacterRepository : CharacterRepository {
    private val delegate = RamCharacterRepository()
    override suspend fun getAll(filter: CharacterFilter?): List<Character> = delegate.getAll(filter)
    override suspend fun get(id: String): Character? = delegate.get(id)
    override suspend fun getByIds(ids: List<String>): List<Character> = delegate.getByIds(ids)
    override suspend fun save(character: Character) = delegate.save(character)
    override suspend fun delete(id: String) = error("Delete failed")
}

private class FailsOnSecondCallCharacterRepository : CharacterRepository {
    private var callCount = 0

    override suspend fun getAll(filter: CharacterFilter?): List<Character> {
        if (callCount++ == 0) return emptyList()
        error("Repository error")
    }

    override suspend fun get(id: String): Character? = null
    override suspend fun getByIds(ids: List<String>): List<Character> = emptyList()
    override suspend fun save(character: Character) = Unit
    override suspend fun delete(id: String) = Unit
}
