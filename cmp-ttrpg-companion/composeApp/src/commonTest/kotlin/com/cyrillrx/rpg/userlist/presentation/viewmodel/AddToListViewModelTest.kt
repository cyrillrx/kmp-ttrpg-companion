package com.cyrillrx.rpg.userlist.presentation.viewmodel

import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.data.SpellEntityRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.userlist.data.RamUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.presentation.AddToListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_spells
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
    private val spellRepository = SpellEntityRepository(SampleSpellRepository())
    private val spell = SampleSpellRepository.getFirst()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(itemId: String = spell.id): AddToListViewModel<Spell> {
        val vm = AddToListViewModel(
            listType = UserList.Type.SPELL,
            userListRepository = userListRepository,
            repository = spellRepository,
            errorMessage = Res.string.error_while_loading_spells,
        )
        vm.loadEntity(itemId)
        return vm
    }

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<AddToListState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when spell is not found`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(itemId = "non-existent-id")

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<AddToListState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = AddToListViewModel(
            listType = UserList.Type.SPELL,
            userListRepository = FailingAddToListRepository(),
            repository = spellRepository,
            errorMessage = Res.string.error_while_loading_spells,
        )
        viewModel.loadEntity(spell.id)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<AddToListState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `initial state loads existing lists of given type`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddToListState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.selectableLists.size)
        assertEquals(expected = "Grimoire", actual = body.selectableLists.first().list.name)
    }

    @Test
    fun `initial state pre-selects lists where item is already added`() = runTest(testDispatcher) {
        val list1 = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf(spell.id))
        val list2 = UserList("list2", "Other", UserList.Type.SPELL, emptyList())
        userListRepository.save(list1)
        userListRepository.save(list2)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddToListState.Body.WithData<Spell>>(viewModel.state.value.body)
        val selectableLists = body.selectableLists
        assertTrue(selectableLists.first { it.list.id == "list1" }.isSelected)
        assertFalse(selectableLists.first { it.list.id == "list2" }.isSelected)
    }

    @Test
    fun `toggleSelection selects the list`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection("list1")

        val body = assertIs<AddToListState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertTrue(body.selectableLists.first { it.list.id == "list1" }.isSelected)
    }

    @Test
    fun `toggleSelection deselects the list`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf(spell.id))
        userListRepository.save(list)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection("list1")

        val body = assertIs<AddToListState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertFalse(body.selectableLists.first { it.list.id == "list1" }.isSelected)
    }

    @Test
    fun `confirmSelection adds item to newly selected lists`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = buildViewModel()

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

        val viewModel = buildViewModel()

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
        val viewModel = buildViewModel()

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
        val viewModel = buildViewModel()

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

        val body = assertIs<AddToListState.Body.WithData<Spell>>(viewModel.state.value.body)
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
