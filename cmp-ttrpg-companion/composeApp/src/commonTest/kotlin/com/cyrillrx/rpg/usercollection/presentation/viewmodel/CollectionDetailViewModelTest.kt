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

private const val TEST_COLLECTION_ID = "collection1"
private const val COLLECTION_NAME = "Name of the collection"
private const val RENAMED_COLLECTION_NAME = "New Name"

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

    private fun buildViewModel(collectionId: String, repo: UserCollectionRepository = userCollectionRepository) =
        CollectionDetailViewModel(collectionId, repo, spellRepository, testDispatcher)

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(TEST_COLLECTION_ID)

        assertIs<CollectionDetailState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(TEST_COLLECTION_ID, FailingUserCollectionRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CollectionDetailState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when collection is not found`() = runTest(testDispatcher) {
        val viewModel = buildViewModel("non_existent_collection")

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CollectionDetailState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `state is Empty when collection has no spells`() = runTest(testDispatcher) {
        val collection = UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, emptyList())
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CollectionDetailState.Body.Empty>(viewModel.state.value.body)
        assertEquals(expected = COLLECTION_NAME, actual = viewModel.state.value.collectionName)
    }

    @Test
    fun `state is WithData when collection has spells`() = runTest(testDispatcher) {
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.items.size)
        assertEquals(expected = spell.id, actual = body.items.first().id)
    }

    @Test
    fun `removeItemOptimistically then commit removes spell from collection`() = runTest(testDispatcher) {
        val spells = SampleSpellRepository.getAll()
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, spells.map { it.id })
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

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
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

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
            val collection =
                UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
            userCollectionRepository.save(collection)

            val viewModel = buildViewModel(TEST_COLLECTION_ID)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect {}
            }

            advanceUntilIdle()

            val pending = requireNotNull(viewModel.removeItemOptimistically(spell.id, spell))
            viewModel.commitRemoval(pending)

            advanceUntilIdle()

            assertIs<CollectionDetailState.Body.Empty>(viewModel.state.value.body)
        }

    @Test
    fun `silentRefresh reflects new items added to repository`() = runTest(testDispatcher) {
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val initialBody = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = initialBody.items.size)

        val updatedCollection = collection.copy(itemIds = collection.itemIds + secondSpell.id)
        userCollectionRepository.save(updatedCollection)

        viewModel.silentRefresh()
        advanceUntilIdle()

        val refreshedBody = assertIs<CollectionDetailState.Body.WithData<Spell>>(viewModel.state.value.body)
        assertEquals(expected = 2, actual = refreshedBody.items.size)
    }

    @Test
    fun `silentRefresh does not transition to Loading state`() = runTest(testDispatcher) {
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

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
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

        assertIs<CollectionDetailState.Body.Loading>(viewModel.state.value.body)

        viewModel.silentRefresh()

        assertIs<CollectionDetailState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `renameCollection updates collection name in state`() = runTest(testDispatcher) {
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.renameCollection(RENAMED_COLLECTION_NAME)
        advanceUntilIdle()

        assertEquals(expected = RENAMED_COLLECTION_NAME, actual = viewModel.state.value.collectionName)
    }

    @Test
    fun `renameCollection persists updated name to repository`() = runTest(testDispatcher) {
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.renameCollection(RENAMED_COLLECTION_NAME)
        advanceUntilIdle()

        val savedCollection = userCollectionRepository.get(TEST_COLLECTION_ID)
        assertEquals(expected = RENAMED_COLLECTION_NAME, actual = savedCollection?.name)
    }

    @Test
    fun `commitRemoval restores item and emits error when repository returns failure`() = runTest(testDispatcher) {
        val failingRepo = FailsOnRemoveUserCollectionRepository()
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        failingRepo.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID, failingRepo)

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
        val collection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, listOf(spell.id))
        userCollectionRepository.save(collection)

        val viewModel = buildViewModel(TEST_COLLECTION_ID)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        advanceUntilIdle()

        viewModel.removeItemOptimistically(spell.id, spell) // no commit
        viewModel.commitAllPendingRemovals()
        advanceUntilIdle()

        val updatedCollection = userCollectionRepository.get(TEST_COLLECTION_ID)!!
        assertTrue(updatedCollection.itemIds.none { it == spell.id })
    }
}

private class FailsOnRemoveUserCollectionRepository : UserCollectionRepository {
    private val delegate = RamUserCollectionRepository()
    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> = delegate.getAll(type)
    override suspend fun get(id: String): UserCollection? = delegate.get(id)
    override suspend fun save(collection: UserCollection) = delegate.save(collection)
    override suspend fun delete(id: String) = delegate.delete(id)
    override suspend fun removeFromCollection(collectionId: String, itemId: String): UserCollectionRepository.Result =
        UserCollectionRepository.Result.Error("Simulated failure")
}
