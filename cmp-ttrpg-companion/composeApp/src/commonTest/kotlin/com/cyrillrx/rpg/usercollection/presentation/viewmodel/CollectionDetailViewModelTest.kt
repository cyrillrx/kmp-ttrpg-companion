package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.usercollection.data.RamUserCollectionRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.CollectionDetailState
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

private const val TEST_LIST_ID = "list1"
private const val LIST_NAME = "Name of the list"
private const val RENAMED_LIST_NAME = "New Name"

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val spellRepository = SampleSpellRepository()
    private val userCollectionRepository = RamUserCollectionRepository()
    private val spell = SampleSpellRepository.getFirst()
    private val secondSpell = SampleSpellRepository.getAll()[1]

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(listId: String, repo: UserCollectionRepository = userCollectionRepository) =
        CollectionDetailViewModel(listId, repo, spellRepository, testDispatcher)

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(TEST_LIST_ID)

        assertIs<CollectionDetailState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(TEST_LIST_ID, FailingUserCollectionRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CollectionDetailState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when list is not found`() = runTest(testDispatcher) {
        val viewModel = buildViewModel("non_existent_list")

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CollectionDetailState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Empty when list has no spells`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, emptyList())
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CollectionDetailState.Body.EmptyList>(viewModel.state.value.body)
        assertEquals(expected = LIST_NAME, actual = viewModel.state.value.listName)
    }

    @Test
    fun `state is WithData when list has spells`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.items.size)
        assertEquals(expected = spell.id, actual = body.items.first().id)
    }

    @Test
    fun `removeItemOptimistically then commit removes spell from list`() = runTest(testDispatcher) {
        val spells = SampleSpellRepository.getAll()
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, spells.map { it.id })
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val pending = requireNotNull(viewModel.removeItemOptimistically(spell.id, spell))
        viewModel.commitRemoval(pending)

        advanceUntilIdle()

        val body = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = spells.size - 1, actual = body.items.size)
        assertTrue(body.items.none { it.id == spell.id })
    }

    @Test
    fun `undoRemoval restores the item`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val pending = requireNotNull(viewModel.removeItemOptimistically(spell.id, spell))
        viewModel.undoRemoval(pending)

        val restoredBody = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(spell, restoredBody.items.first())
    }

    @Test
    fun `removeItemOptimistically then commit transitions to Empty when last spell removed`() =
        runTest(testDispatcher) {
            val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
            userCollectionRepository.save(list)

            val viewModel = buildViewModel(TEST_LIST_ID)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect {}
            }

            advanceUntilIdle()

            val pending = requireNotNull(viewModel.removeItemOptimistically(spell.id, spell))
            viewModel.commitRemoval(pending)

            advanceUntilIdle()

            assertIs<CollectionDetailState.Body.EmptyList>(viewModel.state.value.body)
        }

    @Test
    fun `silentRefresh reflects new items added to repository`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val initialBody = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = initialBody.items.size)

        val updatedList = list.copy(itemIds = list.itemIds + secondSpell.id)
        userCollectionRepository.save(updatedList)

        viewModel.silentRefresh()
        advanceUntilIdle()

        val refreshedBody = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = 2, actual = refreshedBody.items.size)
    }

    @Test
    fun `silentRefresh does not transition to Loading state`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val emittedBodies = mutableListOf<CollectionDetailState.Body<Spell>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { emittedBodies.add(it.body) }
        }

        viewModel.silentRefresh()
        advanceUntilIdle()

        assertTrue(emittedBodies.none { it is CollectionDetailState.Body.Loading })
    }

    @Test
    fun `silentRefresh does nothing when state is already Loading`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        assertIs<CollectionDetailState.Body.Loading>(viewModel.state.value.body)

        viewModel.silentRefresh()

        assertIs<CollectionDetailState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `renameList updates list name in state`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.renameList(RENAMED_LIST_NAME)
        advanceUntilIdle()

        assertEquals(expected = RENAMED_LIST_NAME, actual = viewModel.state.value.listName)
    }

    @Test
    fun `renameList persists updated name to repository`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.renameList(RENAMED_LIST_NAME)
        advanceUntilIdle()

        val savedList = userCollectionRepository.get(TEST_LIST_ID)
        assertEquals(expected = RENAMED_LIST_NAME, actual = savedList?.name)
    }

    @Test
    fun `commitRemoval restores item and emits error when repository returns failure`() = runTest(testDispatcher) {
        val failingRepo = FailsOnRemoveUserCollectionRepository()
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        failingRepo.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID, failingRepo)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val receivedEvents = mutableListOf<CollectionDetailViewModel.Event<Spell>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { receivedEvents.add(it) }
        }

        val pending = requireNotNull(viewModel.removeItemOptimistically(spell.id, spell))
        viewModel.commitRemoval(pending)
        advanceUntilIdle()

        val body = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(spell, body.items.first())
        assertEquals(1, receivedEvents.size)
        assertIs<CollectionDetailViewModel.Event.RemovalError<Spell>>(receivedEvents.first())
    }

    @Test
    fun `commitAllPendingRemovals commits pending removals that were never confirmed`() = runTest(testDispatcher) {
        val list = UserCollection(TEST_LIST_ID, LIST_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(list)

        val viewModel = buildViewModel(TEST_LIST_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        advanceUntilIdle()

        viewModel.removeItemOptimistically(spell.id, spell) // no commit
        viewModel.commitAllPendingRemovals()
        advanceUntilIdle()

        val updatedList = userCollectionRepository.get(TEST_LIST_ID)!!
        assertTrue(updatedList.itemIds.none { it == spell.id })
    }
}

private class FailsOnRemoveUserCollectionRepository : UserCollectionRepository {
    private val delegate = RamUserCollectionRepository()
    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> = delegate.getAll(type)
    override suspend fun get(id: String): UserCollection? = delegate.get(id)
    override suspend fun save(list: UserCollection) = delegate.save(list)
    override suspend fun delete(id: String) = delegate.delete(id)
    override suspend fun removeFromList(listId: String, itemId: String): UserCollectionRepository.Result =
        UserCollectionRepository.Result.Error("Simulated failure")
}
