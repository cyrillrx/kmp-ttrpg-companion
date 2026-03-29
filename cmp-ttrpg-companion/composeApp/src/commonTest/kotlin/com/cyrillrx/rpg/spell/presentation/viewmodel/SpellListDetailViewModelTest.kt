package com.cyrillrx.rpg.spell.presentation.viewmodel

import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListDetailState
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
    fun `state is Empty when list has no spells`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, emptyList())
        userListRepository.save(list)

        val viewModel = SpellListDetailViewModel("list1", userListRepository, spellRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<SpellListDetailState.Body.EmptyList>(viewModel.state.value.body)
        assertEquals(expected = "Grimoire", actual = viewModel.state.value.listName)
    }

    @Test
    fun `state is WithData when list has spells`() = runTest(testDispatcher) {
        val list = UserList("list1", "Sorts de combat", UserList.Type.SPELL, listOf(spell.id))
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
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, allSpells.map { it.id })
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
        assert(body.spells.none { it.id == spell.id })
    }

    @Test
    fun `removeSpell transitions to Empty when last spell removed`() = runTest(testDispatcher) {
        val list = UserList("list1", "Grimoire", UserList.Type.SPELL, listOf(spell.id))
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
