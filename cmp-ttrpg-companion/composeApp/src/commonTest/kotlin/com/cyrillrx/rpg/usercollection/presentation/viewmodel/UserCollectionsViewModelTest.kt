package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.data.RamUserCollectionRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.UserCollectionsState
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
import kotlin.time.Instant

private const val TEST_COLLECTION_ID = "1"
private const val COLLECTION_NAME = "Name of the collection"
private const val UPDATED_COLLECTION_NAME = "Updated name of the collection"

@OptIn(ExperimentalCoroutinesApi::class)
class UserCollectionsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = RamUserCollectionRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(repo: UserCollectionRepository = repository) =
        UserCollectionsViewModel(UserCollection.Type.SPELL, repo, testDispatcher)

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<UserCollectionsState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(FailingUserCollectionRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<UserCollectionsState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `initial state is Empty when no collections exist`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<UserCollectionsState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `createCollection adds a collection and transitions to WithData`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createCollection(COLLECTION_NAME)
        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.collections.size)
        assertEquals(expected = COLLECTION_NAME, actual = body.collections.first().value.name)
        assertEquals(expected = UserCollection.Type.SPELL, actual = body.collections.first().value.type)
    }

    @Test
    fun `deleteCollectionOptimistically removes the collection from UI`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createCollection(COLLECTION_NAME)
        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        val collection = body.collections.first()

        viewModel.deleteCollectionOptimistically(collection)

        assertIs<UserCollectionsState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `undoDeletion restores the collection`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createCollection(COLLECTION_NAME)
        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        val collection = body.collections.first()

        val pending = requireNotNull(viewModel.deleteCollectionOptimistically(collection))
        viewModel.undoDeletion(pending)

        val restoredBody = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(collection, restoredBody.collections.first())
    }

    @Test
    fun `a refresh inside the undo window keeps the collection hidden and undo restores one entry`() =
        runTest(testDispatcher) {
            val viewModel = buildViewModel()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect {}
            }

            advanceUntilIdle()
            viewModel.createCollection(COLLECTION_NAME)
            advanceUntilIdle()

            val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
            val collection = body.collections.first()

            val pending = requireNotNull(viewModel.deleteCollectionOptimistically(collection))
            viewModel.silentRefresh()
            advanceUntilIdle()

            assertIs<UserCollectionsState.Body.Empty>(viewModel.state.value.body)

            viewModel.undoDeletion(pending)

            val restoredBody = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
            assertEquals(expected = 1, actual = restoredBody.collections.size)
            assertEquals(expected = collection.value.id, actual = restoredBody.collections.first().value.id)
        }

    @Test
    fun `a commit failing after a refresh restores one entry and emits an error`() = runTest(testDispatcher) {
        val failingRepo = FailsOnDeleteUserCollectionRepository()
        val viewModel = UserCollectionsViewModel(UserCollection.Type.SPELL, failingRepo, testDispatcher)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createCollection(COLLECTION_NAME)
        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        val collection = body.collections.first()

        val receivedEvents = mutableListOf<UserCollectionsViewModel.Event>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { receivedEvents.add(it) }
        }

        val pending = requireNotNull(viewModel.deleteCollectionOptimistically(collection))
        viewModel.silentRefresh()
        advanceUntilIdle()
        viewModel.commitDeletion(pending)
        advanceUntilIdle()

        val restoredBody = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = restoredBody.collections.size)
        assertEquals(1, receivedEvents.size)
        assertIs<UserCollectionsViewModel.Event.DeletionError>(receivedEvents.first())
    }

    @Test
    fun `commitDeletion removes the collection from repository`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createCollection(COLLECTION_NAME)
        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        val collection = body.collections.first()

        val pending = requireNotNull(viewModel.deleteCollectionOptimistically(collection))
        viewModel.commitDeletion(pending)
        advanceUntilIdle()

        assertTrue(repository.getAll(UserCollection.Type.SPELL).isEmpty())
    }

    @Test
    fun `commitDeletion restores collection and emits error when repository throws`() = runTest(testDispatcher) {
        val failingRepo = FailsOnDeleteUserCollectionRepository()
        val viewModel = UserCollectionsViewModel(UserCollection.Type.SPELL, failingRepo, testDispatcher)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createCollection(COLLECTION_NAME)
        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        val collection = body.collections.first()

        val receivedEvents = mutableListOf<UserCollectionsViewModel.Event>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { receivedEvents.add(it) }
        }

        val pending = requireNotNull(viewModel.deleteCollectionOptimistically(collection))
        viewModel.commitDeletion(pending)
        advanceUntilIdle()

        val restoredBody = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(collection, restoredBody.collections.first())
        assertEquals(1, receivedEvents.size)
        assertIs<UserCollectionsViewModel.Event.DeletionError>(receivedEvents.first())
    }

    @Test
    fun `commitAllPendingDeletions commits pending deletions that were never confirmed`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()
        viewModel.createCollection(COLLECTION_NAME)
        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        val collection = body.collections.first()

        viewModel.deleteCollectionOptimistically(collection) // no commit
        viewModel.commitAllPendingDeletions()
        advanceUntilIdle()

        assertTrue(repository.getAll(UserCollection.Type.SPELL).isEmpty())
    }

    @Test
    fun `a render after commitAllPendingDeletions does not bring the committed collection back`() =
        runTest(testDispatcher) {
            val viewModel = buildViewModel()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect {}
            }

            advanceUntilIdle()
            viewModel.createCollection(COLLECTION_NAME)
            viewModel.createCollection(UPDATED_COLLECTION_NAME)
            advanceUntilIdle()

            val loaded = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body).collections
            assertEquals(expected = 2, actual = loaded.size)
            val committed = loaded.first()
            val kept = loaded.last()

            viewModel.deleteCollectionOptimistically(committed) // no commit
            viewModel.commitAllPendingDeletions()
            advanceUntilIdle()

            val pending = requireNotNull(viewModel.deleteCollectionOptimistically(kept))
            viewModel.undoDeletion(pending)

            val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
            assertEquals(expected = listOf(kept.value.id), actual = body.collections.map { it.value.id })
        }

    @Test
    fun `only collections matching the configured type are shown`() = runTest(testDispatcher) {
        val spellCollection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, emptyList())
        val itemCollection = UserCollection("2", "Artefacts", UserCollection.Type.MAGICAL_ITEM, emptyList())
        repository.save(spellCollection)
        repository.save(itemCollection)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.collections.size)
        assertEquals(expected = spellCollection, actual = body.collections.first().value)
    }

    @Test
    fun `silentRefresh updates state with fresh data without showing Loading`() = runTest(testDispatcher) {
        val spellCollection =
            UserCollection(TEST_COLLECTION_ID, COLLECTION_NAME, UserCollection.Type.SPELL, emptyList())
        repository.save(spellCollection)

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val updatedCollection = spellCollection.copy(name = UPDATED_COLLECTION_NAME)
        repository.save(updatedCollection)

        val emittedBodies = mutableListOf<UserCollectionsState.Body>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { emittedBodies.add(it.body) }
        }

        viewModel.silentRefresh()
        advanceUntilIdle()

        assertTrue(emittedBodies.none { it is UserCollectionsState.Body.Loading })
        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.collections.size)
        assertEquals(expected = UPDATED_COLLECTION_NAME, actual = body.collections.first().value.name)
    }

    @Test
    fun `collections are ordered by updatedAt descending`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(ScrambledUserCollectionRepository())

        advanceUntilIdle()

        val body = assertIs<UserCollectionsState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = listOf("Newest", "Middle", "Oldest"), actual = body.collections.map { it.value.name })
    }

    @Test
    fun `silentRefresh does nothing when state is already Loading`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<UserCollectionsState.Body.Loading>(viewModel.state.value.body)

        viewModel.silentRefresh()

        assertIs<UserCollectionsState.Body.Loading>(viewModel.state.value.body)
    }
}

/** Returns collections whose timestamps deliberately disagree with their position, so only the caller's ordering shows. */
private class ScrambledUserCollectionRepository : UserCollectionRepository {
    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> = listOf(
        stored("Middle", 2_000L),
        stored("Oldest", 1_000L),
        stored("Newest", 3_000L),
    )

    override suspend fun get(id: String): UserCollection? = null
    override suspend fun save(collection: UserCollection) = Unit
    override suspend fun delete(id: String) = Unit

    private fun stored(name: String, epochMillis: Long) = Stored(
        value = UserCollection(id = name, name = name, type = UserCollection.Type.SPELL, itemIds = emptyList()),
        updatedAt = Instant.fromEpochMilliseconds(epochMillis),
    )
}

private class FailsOnDeleteUserCollectionRepository : UserCollectionRepository {
    private val delegate = RamUserCollectionRepository()
    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> = delegate.getAll(type)
    override suspend fun get(id: String): UserCollection? = delegate.get(id)
    override suspend fun save(collection: UserCollection) = delegate.save(collection)
    override suspend fun delete(id: String) = error("Delete failed")
}
