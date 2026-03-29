package com.cyrillrx.rpg.userlist.presentation.viewmodel

import com.cyrillrx.rpg.spell.presentation.viewmodel.UserListsViewModel
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

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = UserListsViewModel(UserList.Type.SPELL, router, repository)

        assertIs<UserListsState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = UserListsViewModel(UserList.Type.SPELL, router, FailingUserListRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<UserListsState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `initial state is Empty when no lists exist`() = runTest(testDispatcher) {
        val viewModel = UserListsViewModel(UserList.Type.SPELL, router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<UserListsState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `createList adds a list and transitions to WithData`() = runTest(testDispatcher) {
        val viewModel = UserListsViewModel(UserList.Type.SPELL, router, repository)

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
    fun `deleteList removes the list`() = runTest(testDispatcher) {
        val viewModel = UserListsViewModel(UserList.Type.SPELL, router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createList("Fighting spells")
        advanceUntilIdle()

        val body = assertIs<UserListsState.Body.WithData>(viewModel.state.value.body)
        val listId = body.lists.first().id

        viewModel.deleteList(listId)
        advanceUntilIdle()

        assertIs<UserListsState.Body.Empty>(viewModel.state.value.body)
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
}

private class FailingUserListRepository : UserListRepository {
    override suspend fun getAll(type: UserList.Type): List<UserList> = error("Repository failure")
    override suspend fun get(id: String): UserList? = null
    override suspend fun save(list: UserList) = Unit
    override suspend fun delete(id: String) = Unit
}

private class TrackingUserListRouter : UserListRouter {
    val openedLists = mutableListOf<UserList>()

    override fun navigateUp() = Unit
    override fun openUserList(list: UserList) {
        openedLists.add(list)
    }
}
