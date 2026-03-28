package com.cyrillrx.rpg.spell.presentation.viewmodel

import com.cyrillrx.rpg.spell.presentation.MySpellListsState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import com.cyrillrx.rpg.spell.domain.Spell
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
class MySpellListsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = RamUserListRepository()
    private val router = NoOpSpellListsRouter()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Empty when no lists exist`() = runTest(testDispatcher) {
        val viewModel = MySpellListsViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<MySpellListsState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `createList adds a list and transitions to WithData`() = runTest(testDispatcher) {
        val viewModel = MySpellListsViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.createList("Sorts de combat")

        advanceUntilIdle()

        val body = assertIs<MySpellListsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.lists.size)
        assertEquals(expected = "Sorts de combat", actual = body.lists.first().name)
        assertEquals(expected = UserList.Type.SPELL, actual = body.lists.first().type)
    }

    @Test
    fun `deleteList removes the list`() = runTest(testDispatcher) {
        val viewModel = MySpellListsViewModel(router, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.createList("Sorts de combat")
        advanceUntilIdle()

        val body = assertIs<MySpellListsState.Body.WithData>(viewModel.state.value.body)
        val listId = body.lists.first().id

        viewModel.deleteList(listId)
        advanceUntilIdle()

        assertIs<MySpellListsState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `openList navigates to spell list detail`() = runTest(testDispatcher) {
        val trackingRouter = TrackingSpellRouter()
        val viewModel = MySpellListsViewModel(trackingRouter, repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.createList("Grimoire")
        advanceUntilIdle()

        val body = assertIs<MySpellListsState.Body.WithData>(viewModel.state.value.body)
        val list = body.lists.first()

        viewModel.openList(list)

        assertTrue(trackingRouter.openedListIds.contains(list.id))
    }
}

private class NoOpSpellListsRouter : SpellRouter {
    override fun navigateUp() = Unit
    override fun openSpellDetail(spell: Spell) = Unit
}

private class TrackingSpellRouter : SpellRouter {
    val openedListIds = mutableListOf<String>()

    override fun navigateUp() = Unit
    override fun openSpellDetail(spell: Spell) = Unit
    override fun openSpellListDetail(listId: String) {
        openedListIds.add(listId)
    }
}
