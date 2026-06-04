package com.cyrillrx.rpg.userlist.presentation.viewmodel

import com.cyrillrx.rpg.userlist.data.RamUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.UserListsState
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

private const val TEST_LIST_ID = "1"
private const val LIST_NAME = "Name of the list"
private const val UPDATED_LIST_NAME = "Updated name of the list"

@OptIn(ExperimentalCoroutinesApi::class)
class UserListsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = RamUserListRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(repo: UserListRepository = repository) =
        UserListsViewModel(UserList.Type.SPELL, repo, testDispatcher)

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<UserListsState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(FailingUserListRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<UserListsState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `initial state is Empty when no lists exist`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<UserListsState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `createList adds a list and transitions to WithData`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList(LIST_NAME)
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.lists.size)
        assertEquals(expected = LIST_NAME, actual = body.lists.first().name)
        assertEquals(expected = UserList.Type.SPELL, actual = body.lists.first().type)
    }

    @Test
    fun `deleteListOptimistically removes the list from UI`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList(LIST_NAME)
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        viewModel.deleteListOptimistically(list)

        assertIs<UserListsState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `undoDeletion restores the list`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList(LIST_NAME)
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        val pending = requireNotNull(viewModel.deleteListOptimistically(list))
        viewModel.undoDeletion(pending)

        val restoredBody = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(list, restoredBody.lists.first())
    }

    @Test
    fun `commitDeletion removes the list from repository`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList(LIST_NAME)
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        val pending = requireNotNull(viewModel.deleteListOptimistically(list))
        viewModel.commitDeletion(pending)
        advanceUntilIdle()

        assertTrue(repository.getAll(UserList.Type.SPELL).isEmpty())
    }

    @Test
    fun `commitDeletion restores list and emits error when repository throws`() = runTest(testDispatcher) {
        val failingRepo = FailsOnDeleteUserListRepository()
        val viewModel = UserListsViewModel(UserList.Type.SPELL, failingRepo, testDispatcher)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList(LIST_NAME)
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        val receivedEvents = mutableListOf<UserListsViewModel.Event>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { receivedEvents.add(it) }
        }

        val pending = requireNotNull(viewModel.deleteListOptimistically(list))
        viewModel.commitDeletion(pending)
        advanceUntilIdle()

        val restoredBody = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(list, restoredBody.lists.first())
        assertEquals(1, receivedEvents.size)
        assertIs<UserListsViewModel.Event.DeletionError>(receivedEvents.first())
    }

    @Test
    fun `commitAllPendingDeletions commits pending deletions that were never confirmed`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList(LIST_NAME)
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        viewModel.deleteListOptimistically(list) // no commit
        viewModel.commitAllPendingDeletions()
        advanceUntilIdle()

        assertTrue(repository.getAll(UserList.Type.SPELL).isEmpty())
    }

    @Test
    fun `only lists matching the configured type are shown`() = runTest(testDispatcher) {
        val spellList = UserList(TEST_LIST_ID, LIST_NAME, UserList.Type.SPELL, emptyList())
        val itemList = UserList("2", "Artefacts", UserList.Type.MAGICAL_ITEM, emptyList())
        repository.save(spellList)
        repository.save(itemList)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.lists.size)
        assertEquals(expected = spellList, actual = body.lists.first())
    }

    @Test
    fun `silentRefresh updates state with fresh data without showing Loading`() = runTest(testDispatcher) {
        val spellList = UserList(TEST_LIST_ID, LIST_NAME, UserList.Type.SPELL, emptyList())
        repository.save(spellList)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val updatedList = spellList.copy(name = UPDATED_LIST_NAME)
        repository.save(updatedList)

        val emittedBodies = mutableListOf<UserListsState.Body>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { emittedBodies.add(it.body) }
        }

        viewModel.silentRefresh()
        advanceUntilIdle()

        assertTrue(emittedBodies.none { it is UserListsState.Body.Loading })
        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.lists.size)
        assertEquals(expected = UPDATED_LIST_NAME, actual = body.lists.first().name)
    }

    @Test
    fun `silentRefresh does nothing when state is already Loading`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<UserListsState.Body.Loading>(viewModel.state.value.body)

        viewModel.silentRefresh()

        assertIs<UserListsState.Body.Loading>(viewModel.state.value.body)
    }

}

private class FailsOnDeleteUserListRepository : UserListRepository {
    private val delegate = RamUserListRepository()
    override suspend fun getAll(type: UserList.Type): List<UserList> = delegate.getAll(type)
    override suspend fun get(id: String): UserList? = delegate.get(id)
    override suspend fun save(list: UserList) = delegate.save(list)
    override suspend fun delete(id: String) = error("Delete failed")
}

