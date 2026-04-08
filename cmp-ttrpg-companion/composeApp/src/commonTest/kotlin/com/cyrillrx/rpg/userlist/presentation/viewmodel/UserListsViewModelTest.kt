package com.cyrillrx.rpg.userlist.presentation.viewmodel

import com.cyrillrx.rpg.userlist.data.RamUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.UserListsState
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouter
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
class UserListsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = RamUserListRepository()
    private val router = NoOpUserListRouter()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(repo: UserListRepository = repository) =
        UserListsViewModel(UserList.Type.SPELL, router, repo, testDispatcher)

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
        viewModel.createList("Fighting spells")
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.lists.size)
        assertEquals(expected = "Fighting spells", actual = body.lists.first().name)
        assertEquals(expected = UserList.Type.SPELL, actual = body.lists.first().type)
    }

    @Test
    fun `deleteListOptimistically removes the list from UI`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList("Fighting spells")
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
        viewModel.createList("Fighting spells")
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
        viewModel.createList("Fighting spells")
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        val pending = requireNotNull(viewModel.deleteListOptimistically(list))
        viewModel.commitDeletion(pending)
        advanceUntilIdle()

        assertTrue(repository.getAll(UserList.Type.SPELL).isEmpty())
    }

    @Test
    fun `commitAllPendingDeletions commits pending deletions`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList("Fighting spells")
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        viewModel.deleteListOptimistically(list) // no commit
        viewModel.commitAllPendingDeletions() // simulate onCleared
        advanceUntilIdle()

        assertTrue(repository.getAll(UserList.Type.SPELL).isEmpty())
    }

    @Test
    fun `only lists matching the configured type are shown`() = runTest(testDispatcher) {
        val spellList = UserList("1", "Spellbook", UserList.Type.SPELL, emptyList())
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
    fun `refresh updates state with fresh data without showing Loading`() = runTest(testDispatcher) {
        val spellList = UserList("1", "Spellbook", UserList.Type.SPELL, emptyList())
        repository.save(spellList)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val updatedList = spellList.copy(name = "Updated Spellbook")
        repository.save(updatedList)

        viewModel.silentRefresh()

        // State must not be Loading during refresh
        assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)

        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.lists.size)
        assertEquals(expected = "Updated Spellbook", actual = body.lists.first().name)
    }

    @Test
    fun `openList delegates to router`() = runTest(testDispatcher) {
        val trackingRouter = TrackingUserListRouter()
        val viewModel = UserListsViewModel(UserList.Type.SPELL, trackingRouter, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList("Spellbook")
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        viewModel.openList(list)

        assertTrue(trackingRouter.openedLists.contains(list))
    }
}

private class NoOpUserListRouter : UserListRouter {
    override fun navigateUp() = Unit
    override fun openUserList(list: UserList) = Unit
}

private class TrackingUserListRouter : UserListRouter {
    val openedLists = mutableListOf<UserList>()

    override fun navigateUp() = Unit
    override fun openUserList(list: UserList) {
        openedLists.add(list)
    }
}
