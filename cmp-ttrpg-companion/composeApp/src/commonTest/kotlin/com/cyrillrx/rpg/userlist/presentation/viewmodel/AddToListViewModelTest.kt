package com.cyrillrx.rpg.userlist.presentation.viewmodel

import com.cyrillrx.rpg.core.presentation.AddToListState
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
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
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddToListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val userListRepository = RamUserListRepository()
    private val spellRepository = SampleSpellRepository()
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
    fun `initial state loads existing lists of given type`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddToListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.lists.size)
        assertEquals(expected = "Grimoire", actual = body.lists.first().list.name)
    }

    @Test
    fun `initial state pre-selects lists where item is already added`() = runTest(testDispatcher) {
        val list1 = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf(spell.id))
        val list2 = UserList("list2", "Other", UserList.Type.SPELL, emptyList())
        userListRepository.save(list1)
        userListRepository.save(list2)

        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddToListState.Body.WithData>(viewModel.state.value.body)
        assertTrue("list1" in body.pendingSelection)
        assertFalse("list2" in body.pendingSelection)
    }

    @Test
    fun `toggleSelection adds listId to pendingSelection`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection("list1")

        val body = assertIs<AddToListState.Body.WithData>(viewModel.state.value.body)
        assertTrue("list1" in body.pendingSelection)
    }

    @Test
    fun `toggleSelection removes listId from pendingSelection`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf(spell.id))
        userListRepository.save(list)

        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val bodyBefore = assertIs<AddToListState.Body.WithData>(viewModel.state.value.body)
        assertTrue("list1" in bodyBefore.pendingSelection)

        viewModel.toggleSelection("list1")

        val bodyAfter = assertIs<AddToListState.Body.WithData>(viewModel.state.value.body)
        assertFalse("list1" in bodyAfter.pendingSelection)
    }

    @Test
    fun `confirmSelection adds item to newly selected lists`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection("list1")
        viewModel.confirmSelection()

        advanceUntilIdle()

        val savedList = userListRepository.get("list1")
        assertTrue(savedList?.itemIds?.contains(spell.id) ?: false)
    }

    @Test
    fun `confirmSelection removes item from deselected lists`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf(spell.id))
        userListRepository.save(list)

        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection("list1")
        viewModel.confirmSelection()

        advanceUntilIdle()

        val savedList = userListRepository.get("list1")
        assertFalse(savedList?.itemIds?.contains(spell.id) ?: true)
    }

    @Test
    fun `confirmSelection emits Dismiss`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        val events = mutableListOf<AddToListViewModel.Event>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { events.add(it) }
        }

        advanceUntilIdle()

        viewModel.confirmSelection()

        advanceUntilIdle()

        assertTrue(events.isNotEmpty())
        assertIs<AddToListViewModel.Event.Dismiss>(events.first())
    }

    @Test
    fun `createAndAdd creates a new list with the itemId`() = runTest(testDispatcher) {
        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        val events = mutableListOf<AddToListViewModel.Event>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { events.add(it) }
        }

        advanceUntilIdle()

        viewModel.createAndAdd("Nouveau grimoire")

        advanceUntilIdle()

        assertTrue(events.isNotEmpty())
        val lists = userListRepository.getAll(UserList.Type.SPELL)
        assertEquals(expected = 1, actual = lists.size)
        assertEquals(expected = "Nouveau grimoire", actual = lists.first().name)
        assertTrue(lists.first().itemIds.contains(spell.id))
    }

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        assertIs<AddToListState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when spell is not found`() = runTest(testDispatcher) {
        val viewModel = AddToListViewModel("non-existent-id", UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<AddToListState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = AddToListViewModel(spell.id, UserList.Type.SPELL, FailingAddToListRepository(), spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<AddToListState.Body.Error>(viewModel.state.value.body)
    }
}

private class FailingAddToListRepository : com.cyrillrx.rpg.userlist.domain.UserListRepository {
    override suspend fun getAll(type: UserList.Type): List<UserList> = error("Repository failure")
    override suspend fun get(id: String): UserList? = error("Repository failure")
    override suspend fun save(list: UserList) = Unit
    override suspend fun delete(id: String) = Unit
}
