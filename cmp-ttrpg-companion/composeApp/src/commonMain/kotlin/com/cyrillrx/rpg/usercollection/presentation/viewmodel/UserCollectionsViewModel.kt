package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.presentation.commitAllPending
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.UserCollectionsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_collections
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserCollectionsViewModel(
    private val collectionType: UserCollection.Type,
    private val userCollectionRepository: UserCollectionRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val state: StateFlow<UserCollectionsState>
        field = MutableStateFlow(UserCollectionsState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    data class PendingDeletion(val stored: Stored<UserCollection>)

    sealed interface Event {
        data class DeletionError(val collection: UserCollection) : Event
    }

    private val pendingDeletions: MutableList<PendingDeletion> = mutableListOf()

    /**
     * Last list read from the repository. The rendered body is derived from it by subtracting the
     * pending deletions, so a refresh landing inside the undo window cannot resurrect a swiped row.
     */
    private var loadedCollections: List<Stored<UserCollection>> = emptyList()
    private var activeJob: Job? = null

    init {
        activeJob = loadCollections()
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createCollection(name: String) {
        viewModelScope.launch {
            val newCollection = UserCollection(
                id = Uuid.random().toString(),
                name = name,
                type = collectionType,
                itemIds = emptyList(),
            )
            userCollectionRepository.save(newCollection)
            activeJob?.cancel()
            activeJob = loadCollections()
        }
    }

    fun deleteCollectionOptimistically(stored: Stored<UserCollection>): PendingDeletion? {
        val body = state.value.body as? UserCollectionsState.Body.WithData ?: return null
        if (stored !in body.collections) return null

        val pending = PendingDeletion(stored)
        pendingDeletions.add(pending)
        renderBody()
        return pending
    }

    fun undoDeletion(pending: PendingDeletion) {
        if (!pendingDeletions.remove(pending)) return

        renderBody()
    }

    fun commitDeletion(pending: PendingDeletion) {
        if (!pendingDeletions.remove(pending)) return

        val deletedId = pending.stored.value.id
        viewModelScope.launch {
            val deleted = try {
                userCollectionRepository.delete(deletedId)
                true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                false
            }
            if (deleted) loadedCollections = loadedCollections.filterNot { it.value.id == deletedId }
            renderBody()
            if (!deleted) events.emit(Event.DeletionError(pending.stored.value))
        }
    }

    internal fun commitAllPendingDeletions() {
        pendingDeletions.commitAllPending(ioDispatcher) { pending ->
            userCollectionRepository.delete(pending.stored.value.id)
        }
    }

    fun silentRefresh() {
        if (state.value.body is UserCollectionsState.Body.Loading) return
        activeJob?.cancel()
        activeJob = refreshCollections()
    }

    private fun refreshCollections(): Job =
        viewModelScope.launch {
            try {
                fetchAndUpdateUserCollections()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Keep existing state on refresh failure
            }
        }

    private fun loadCollections(): Job =
        viewModelScope.launch {
            state.update { it.copy(body = UserCollectionsState.Body.Loading) }
            try {
                fetchAndUpdateUserCollections()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        body = UserCollectionsState.Body.Error(
                            errorMessage = Res.string.error_while_loading_collections,
                        ),
                    )
                }
            }
        }

    private suspend fun fetchAndUpdateUserCollections() {
        loadedCollections = userCollectionRepository.getAll(collectionType).sortedByDescending { it.updatedAt }
        renderBody()
    }

    private fun renderBody() {
        val hiddenIds = pendingDeletions.mapTo(mutableSetOf()) { it.stored.value.id }
        val visibleCollections = loadedCollections.filterNot { it.value.id in hiddenIds }
        val body = if (visibleCollections.isEmpty()) {
            UserCollectionsState.Body.Empty
        } else {
            UserCollectionsState.Body.WithData(visibleCollections)
        }
        state.update { it.copy(body = body) }
    }
}
