package com.cyrillrx.rpg.spell.presentation.viewmodel

import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListDetailState
import com.cyrillrx.rpg.userlist.data.RamUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
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
class SpellListDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val spellRepository = SampleSpellRepository()
    private val userListRepository = RamUserListRepository()
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
        val viewModel = SpellListDetailViewModel("list1", userListRepository, spellRepository)

        assertIs<SpellListDetailState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = SpellListDetailViewModel("list1", FailingUserListRepository(), spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<SpellListDetailState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when list is not found`() = runTest(testDispatcher) {
        val viewModel = SpellListDetailViewModel("non_existent_list", userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<SpellListDetailState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Empty when list has no spells`() = runTest(testDispatcher) {
        val list = UserList("list1", "Gandalf's spells", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = SpellListDetailViewModel("list1", userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<SpellListDetailState.Body.EmptyList>(viewModel.state.value.body)
        assertEquals(expected = "Gandalf's spells", actual = viewModel.state.value.listName)
    }

    @Test
    fun `state is WithData when list has spells`() = runTest(testDispatcher) {
        val list = UserList("list1", "Fighting spells", UserList.Type.SPELL, listOf(spell.id))
        userListRepository.save(list)

        val viewModel = SpellListDetailViewModel("list1", userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<SpellListDetailState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.spells.size)
        assertEquals(expected = spell.id, actual = body.spells.first().id)
    }

    @Test
    fun `removeSpell removes spell from list`() = runTest(testDispatcher) {
        val allSpells = SampleSpellRepository.getAll()
        val list = UserList("list1", "My Spellbook", UserList.Type.SPELL, allSpells.map { it.id })
        userListRepository.save(list)

        val viewModel = SpellListDetailViewModel("list1", userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.removeSpell(spell.id)

        advanceUntilIdle()

        val body = assertIs<SpellListDetailState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = allSpells.size - 1, actual = body.spells.size)
        assertTrue(body.spells.none { it.id == spell.id })
    }

    @Test
    fun `removeSpell transitions to Empty when last spell removed`() = runTest(testDispatcher) {
        val list = UserList("list1", "My spellbook", UserList.Type.SPELL, listOf(spell.id))
        userListRepository.save(list)

        val viewModel = SpellListDetailViewModel("list1", userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.removeSpell(spell.id)

        advanceUntilIdle()

        assertIs<SpellListDetailState.Body.EmptyList>(viewModel.state.value.body)
    }
}

private class FailingUserListRepository : UserListRepository {
    override suspend fun getAll(type: UserList.Type): List<UserList> = error("Repository failure")
    override suspend fun get(id: String): UserList = error("Repository failure")
    override suspend fun save(list: UserList) = Unit
    override suspend fun delete(id: String) = Unit
}
