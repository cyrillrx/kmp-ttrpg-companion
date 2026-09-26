package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.presentation.OptimisticDeletions
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.UserCollectionsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_collections
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserCollectionsViewModel(
    private val collectionType: UserCollection.ItemType,
    private val userCollectionRepository: UserCollectionRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val state: StateFlow<UserCollectionsState>
        field = MutableStateFlow(UserCollectionsState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    sealed interface Event {
        data class DeletionError(val collection: UserCollection) : Event
        data class CreationError(val name: String) : Event
    }

    private val deletions = OptimisticDeletions<Stored<UserCollection>> { it.value.id }

    /**
     * Detached from [viewModelScope] on purpose: a commit started when the snackbar expired must reach
     * the repository even if the user leaves the screen while the call is in flight.
     */
    private val commitScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

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
                itemType = collectionType,
                itemIds = emptyList(),
            )
            try {
                userCollectionRepository.save(newCollection)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                events.emit(Event.CreationError(name))
                return@launch
            }
            activeJob?.cancel()
            activeJob = loadCollections()
        }
    }

    fun deleteCollectionOptimistically(
        stored: Stored<UserCollection>,
    ): OptimisticDeletions.Pending<Stored<UserCollection>>? {
        if (state.value.body !is UserCollectionsState.Body.WithData) return null

        val pending = deletions.hide(stored) ?: return null
        renderBody()
        return pending
    }

    fun undoDeletion(pending: OptimisticDeletions.Pending<Stored<UserCollection>>) {
        if (!deletions.undo(pending)) return

        renderBodyIfLoaded()
    }

    fun commitDeletion(pending: OptimisticDeletions.Pending<Stored<UserCollection>>) {
        if (!deletions.claim(pending)) return

        commit(pending)
    }

    internal fun commitAllPendingDeletions() {
        deletions.claimAll().forEach(::commit)
    }

    private fun commit(pending: OptimisticDeletions.Pending<Stored<UserCollection>>) {
        commitScope.launch {
            val deleted = try {
                withContext(ioDispatcher) { userCollectionRepository.delete(pending.item.value.id) }
                true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                false
            }
            deletions.settle(pending, deleted)
            renderBodyIfLoaded()
            if (!deleted) events.emit(Event.DeletionError(pending.item.value))
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
        deletions.setLoaded(userCollectionRepository.getAll(collectionType).sortedByDescending { it.updatedAt })
        renderBody()
    }

    /**
     * Rendering over a `Loading` or `Error` body would bury the pending fetch or the error message
     * under the previous read. [renderBody] cannot hold the guard itself: a load sets `Loading`
     * first and relies on its own fetch to render over it.
     */
    private fun renderBodyIfLoaded() {
        val body = state.value.body
        if (body !is UserCollectionsState.Body.WithData && body !is UserCollectionsState.Body.Empty) return

        renderBody()
    }

    private fun renderBody() {
        val visible = deletions.visible
        val body = if (visible.isEmpty()) {
            UserCollectionsState.Body.Empty
        } else {
            UserCollectionsState.Body.WithData(visible)
        }
        state.update { it.copy(body = body) }
    }
}
