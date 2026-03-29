package com.cyrillrx.rpg.userlist.presentation.viewmodel

import com.cyrillrx.rpg.core.presentation.AddToListState
import com.cyrillrx.rpg.userlist.data.RamUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
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
class AddToListViewModelTest {

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

    @Test
    fun `initial state loads existing lists of given type`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        repository.save(list)

        val viewModel = AddToListViewModel("spell1", UserList.Type.SPELL, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddToListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.lists.size)
        assertEquals(expected = "Grimoire", actual = body.lists.first().name)
    }

    @Test
    fun `addToList appends itemId to existing list`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        repository.save(list)

        val viewModel = AddToListViewModel("spell1", UserList.Type.SPELL, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        var successCalled = false
        viewModel.addToList("list1") { successCalled = true }

        advanceUntilIdle()

        assertTrue(successCalled)
        val savedList = repository.get("list1")
        assertTrue(savedList?.itemIds?.contains("spell1") == true)
    }

    @Test
    fun `addToList does not duplicate itemId`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf("spell1"))
        repository.save(list)

        val viewModel = AddToListViewModel("spell1", UserList.Type.SPELL, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.addToList("list1") {}

        advanceUntilIdle()

        val savedList = repository.get("list1")
        assertEquals(expected = 1, actual = savedList?.itemIds?.size)
    }

    @Test
    fun `createAndAdd creates a new list with the itemId`() = runTest(testDispatcher) {
        val viewModel = AddToListViewModel("spell1", UserList.Type.SPELL, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        var successCalled = false
        viewModel.createAndAdd("Nouveau grimoire") { successCalled = true }

        advanceUntilIdle()

        assertTrue(successCalled)
        val lists = repository.getAll(UserList.Type.SPELL)
        assertEquals(expected = 1, actual = lists.size)
        assertEquals(expected = "Nouveau grimoire", actual = lists.first().name)
        assertTrue(lists.first().itemIds.contains("spell1"))
    }
}