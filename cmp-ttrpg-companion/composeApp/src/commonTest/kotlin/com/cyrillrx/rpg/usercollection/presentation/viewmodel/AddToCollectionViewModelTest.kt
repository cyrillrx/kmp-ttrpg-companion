package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.usercollection.data.RamUserCollectionRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.presentation.AddToCollectionState
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

private const val TEST_LIST_ID = "list1"
private const val TEST_LIST_ID_2 = "list2"
private const val LIST_NAME = "Grimoire"
private const val CREATED_LIST_NAME = "Nouveau grimoire"

@OptIn(ExperimentalCoroutinesApi::class)
class AddToCollectionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val userCollectionRepository = RamUserCollectionRepository()
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

    private fun buildViewModel(itemId: String = spell.id): AddToCollectionViewModel<Spell> {
        val vm = AddToCollectionViewModel(
            listType = UserCollection.Type.SPELL,
            userCollectionRepository = userCollectionRepository,
            repository = spellRepository,
            errorMessage = Res.string.error_while_loading_spells,
        )
        vm.loadEntity(itemId)
        return vm
    }

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<AddToCollectionState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when spell is not found`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(itemId = "non-existent-id")

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<AddToCollectionState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = AddToCollectionViewModel(
            listType = UserCollection.Type.SPELL,
            userCollectionRepository = FailingAddToCollectionRepository(),
            repository = spellRepository,
            errorMessage = Res.string.error_while_loading_spells,
        )
        viewModel.loadEntity(spell.id)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<AddToCollectionState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `initial state loads existing lists of given type`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, emptyList())
        userCollectionRepository.save(list)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddToCollectionState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.selectableCollections.size)
        assertEquals(expected = LIST_NAME, actual = body.selectableCollections.first().list.name)
    }

    @Test
    fun `initial state pre-selects lists where item is already added`() = runTest(testDispatcher) {
        val list1 = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        val list2 = UserCollection(TEST_LIST_ID_2, "Other", UserCollection.Type.SPELL, emptyList())
        userCollectionRepository.save(list1)
        userCollectionRepository.save(list2)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<AddToCollectionState.Body.WithData<Spell>>(viewModel.state.value.body)
        val selectableCollections = body.selectableCollections
        assertTrue(selectableCollections.first { it.list.id == TEST_LIST_ID }.isSelected)
        assertFalse(selectableCollections.first { it.list.id == TEST_LIST_ID_2 }.isSelected)
    }

    @Test
    fun `toggleSelection selects the list`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, emptyList())
        userCollectionRepository.save(list)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection(TEST_LIST_ID)

        val body = assertIs<AddToCollectionState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertTrue(body.selectableCollections.first { it.list.id == TEST_LIST_ID }.isSelected)
    }

    @Test
    fun `toggleSelection deselects the list`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection(TEST_LIST_ID)

        val body = assertIs<AddToCollectionState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertFalse(body.selectableCollections.first { it.list.id == TEST_LIST_ID }.isSelected)
    }

    @Test
    fun `confirmSelection adds item to newly selected lists`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, emptyList())
        userCollectionRepository.save(list)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection(TEST_LIST_ID)
        viewModel.confirmSelection()

        advanceUntilIdle()

        val savedList = userCollectionRepository.get(TEST_LIST_ID)
        assertTrue(savedList?.itemIds?.contains(spell.id) ?: false)
    }

    @Test
    fun `confirmSelection removes item from deselected lists`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.toggleSelection(TEST_LIST_ID)
        viewModel.confirmSelection()

        advanceUntilIdle()

        val savedList = userCollectionRepository.get(TEST_LIST_ID)
        assertFalse(savedList?.itemIds?.contains(spell.id) ?: true)
    }

    @Test
    fun `confirmSelection emits Dismiss`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        val events = mutableListOf<AddToCollectionViewModel.Event>()
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
        assertIs<AddToCollectionViewModel.Event.Dismiss>(events.first())
    }

    @Test
    fun `loadEntity reflects fresh list data when called again after repository changes`() =
        runTest(testDispatcher) {
            val viewModel = buildViewModel()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect {}
            }

            advanceUntilIdle()

            val initialBody = assertIs<AddToCollectionState.Body.WithData<Spell>>(viewModel.state.value.body)
            assertTrue(initialBody.selectableCollections.isEmpty())

            // Simulate: spell was added to a new list externally (e.g. from another screen)
            val newList = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
            userCollectionRepository.save(newList)

            // Re-call loadEntity — simulates the bottom sheet being re-opened
            viewModel.loadEntity(spell.id)
            advanceUntilIdle()

            val refreshedBody = assertIs<AddToCollectionState.Body.WithData<Spell>>(viewModel.state.value.body)
            assertEquals(expected = 1, actual = refreshedBody.selectableCollections.size)
            assertTrue(refreshedBody.selectableCollections.first().alreadyAdded)
        }

    @Test
    fun `createAndAdd creates a new list with the itemId`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.createAndAdd(CREATED_LIST_NAME)

        advanceUntilIdle()

        val lists = userCollectionRepository.getAll(UserCollection.Type.SPELL)
        assertEquals(expected = 1, actual = lists.size)
        assertEquals(expected = CREATED_LIST_NAME, actual = lists.first().value.name)
        assertTrue(actual = lists.first().value.itemIds.contains(spell.id))

        val body = assertIs<AddToCollectionState.Body.WithData<Spell>>(viewModel.state.value.body)
        val newEntry = body.selectableCollections.first { it.list.name == CREATED_LIST_NAME }
        assertTrue(newEntry.alreadyAdded)
        assertTrue(newEntry.isSelected)
    }
}

private class FailingAddToCollectionRepository : com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository {
    override suspend fun getAll(type: UserCollection.Type): List<com.cyrillrx.rpg.core.domain.Stored<UserCollection>> =
        error("Repository failure")
    override suspend fun get(id: String): UserCollection? = error("Repository failure")
    override suspend fun save(list: UserCollection) = Unit
    override suspend fun delete(id: String) = Unit
}
