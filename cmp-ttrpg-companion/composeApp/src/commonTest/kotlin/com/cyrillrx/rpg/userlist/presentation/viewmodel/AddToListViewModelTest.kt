package com.cyrillrx.rpg.userlist.presentation.viewmodel

import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.userlist.data.RamUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.presentation.AddSpellToListState
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
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        assertIs<AddSpellToListState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when spell is not found`() = runTest(testDispatcher) {
        val viewModel = AddSpellToListViewModel("non-existent-id", UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<AddSpellToListState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, FailingAddToListRepository(), spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<AddSpellToListState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `initial state loads existing lists of given type`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddSpellToListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.selectableLists.size)
        assertEquals(expected = "Grimoire", actual = body.selectableLists.first().list.name)
    }

    @Test
    fun `initial state pre-selects lists where item is already added`() = runTest(testDispatcher) {
        val list1 = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf(spell.id))
        val list2 = UserList("list2", "Other", UserList.Type.SPELL, emptyList())
        userListRepository.save(list1)
        userListRepository.save(list2)

        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddSpellToListState.Body.WithData>(viewModel.state.value.body)
        val selectableLists = body.selectableLists
        val selectableList1 = selectableLists.first { it.list.id == "list1" }
        val selectableList2 = selectableLists.first { it.list.id == "list2" }
        assertTrue(selectableList1.isSelected)
        assertFalse(selectableList2.isSelected)
    }

    @Test
    fun `toggleSelection selects the list`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection("list1")

        val body = assertIs<AddSpellToListState.Body.WithData>(viewModel.state.value.body)
        assertTrue(body.selectableLists.first { it.list.id == "list1" }.isSelected)
    }

    @Test
    fun `toggleSelection deselects the list`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf(spell.id))
        userListRepository.save(list)

        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection("list1")

        val bodyAfter = assertIs<AddSpellToListState.Body.WithData>(viewModel.state.value.body)
        assertFalse(bodyAfter.selectableLists.first { it.list.id == "list1" }.isSelected)
    }

    @Test
    fun `confirmSelection adds item to newly selected lists`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

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

        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

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
        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        val events = mutableListOf<AddSpellToListViewModel.Event>()
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
        assertIs<AddSpellToListViewModel.Event.Dismiss>(events.first())
    }

    @Test
    fun `createAndAdd creates a new list with the itemId`() = runTest(testDispatcher) {
        val viewModel = AddSpellToListViewModel(spell.id, UserList.Type.SPELL, userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.createAndAdd("Nouveau grimoire")

        advanceUntilIdle()

        val lists = userListRepository.getAll(UserList.Type.SPELL)
        assertEquals(expected = 1, actual = lists.size)
        assertEquals(expected = "Nouveau grimoire", actual = lists.first().name)
        assertTrue(actual = lists.first().itemIds.contains(spell.id))

        val body = assertIs<AddSpellToListState.Body.WithData>(viewModel.state.value.body)
        val newEntry = body.selectableLists.first { it.list.name == "Nouveau grimoire" }
        assertTrue(newEntry.alreadyAdded)
        assertTrue(newEntry.isSelected)
    }
}

private class FailingAddToListRepository : com.cyrillrx.rpg.userlist.domain.UserListRepository {
    override suspend fun getAll(type: UserList.Type): List<UserList> = error("Repository failure")
    override suspend fun get(id: String): UserList? = error("Repository failure")
    override suspend fun save(list: UserList) = Unit
    override suspend fun delete(id: String) = Unit
}
