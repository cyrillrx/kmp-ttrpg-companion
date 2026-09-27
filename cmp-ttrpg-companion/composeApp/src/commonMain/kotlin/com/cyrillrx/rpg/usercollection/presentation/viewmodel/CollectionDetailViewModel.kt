package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.Entity
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.core.presentation.OptimisticDeletions
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.CollectionDetailState
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
import rpg_companion.composeapp.generated.resources.error_while_loading_collection
import kotlin.coroutines.cancellation.CancellationException

class CollectionDetailViewModel<T : Entity>(
    private val collectionId: String,
    private val userCollectionRepository: UserCollectionRepository,
    private val repository: EntityRepository<T>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val state: StateFlow<CollectionDetailState<T>>
        field = MutableStateFlow(CollectionDetailState())

    val events: SharedFlow<Event<T>>
        field = MutableSharedFlow<Event<T>>()

    sealed interface Event<out T> {
        data class RemovalError<T>(val item: T) : Event<T>
        data object RenameError : Event<Nothing>
    }

    private val removals = OptimisticDeletions<T> { it.id }

    /**
     * Detached from [viewModelScope] on purpose: a commit started when the snackbar expired must reach
     * the repository even if the user leaves the screen while the call is in flight.
     */
    private val commitScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var activeJob: Job? = null

    init {
        activeJob = loadDetail()
    }

    fun renameCollection(newName: String) {
        viewModelScope.launch {
            try {
                val result = userCollectionRepository.rename(collectionId, newName)
                if (result is UserCollectionRepository.Result.Success) {
                    state.update { it.copy(collectionName = newName) }
                } else {
                    events.emit(Event.RenameError)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                events.emit(Event.RenameError)
            }
        }
    }

    fun removeItemOptimistically(item: T): OptimisticDeletions.Pending<T>? {
        if (state.value.body !is CollectionDetailState.Body.WithData) return null

        val pending = removals.hide(item) ?: return null
        renderBody()
        return pending
    }

    fun undoRemoval(pending: OptimisticDeletions.Pending<T>) {
        if (!removals.undo(pending)) return

        renderBodyIfLoaded()
    }

    fun commitRemoval(pending: OptimisticDeletions.Pending<T>) {
        if (!removals.claim(pending)) return

        commit(pending)
    }

    internal fun commitAllPendingRemovals() {
        removals.claimAll().forEach(::commit)
    }

    private fun commit(pending: OptimisticDeletions.Pending<T>) {
        commitScope.launch {
            val result = try {
                withContext(ioDispatcher) {
                    userCollectionRepository.removeFromCollection(collectionId, pending.item.id)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                UserCollectionRepository.Result.Error(e.message ?: "removal failed")
            }
            val removed = result is UserCollectionRepository.Result.Success
            removals.settle(pending, removed)
            renderBodyIfLoaded()
            if (!removed) events.emit(Event.RemovalError(pending.item))
        }
    }

    fun silentRefresh() {
        if (state.value.body is CollectionDetailState.Body.Loading) return
        activeJob?.cancel()
        activeJob = refreshDetail()
    }

    private fun refreshDetail(): Job =
        viewModelScope.launch {
            try {
                fetchDetail()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Silently ignore — don't overwrite existing content with an error
            }
        }

    private fun loadDetail(): Job =
        viewModelScope.launch {
            state.update { it.copy(body = CollectionDetailState.Body.Loading) }
            try {
                fetchDetail()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update {
                    it.copy(body = CollectionDetailState.Body.Error(Res.string.error_while_loading_collection))
                }
            }
        }

    private suspend fun fetchDetail() {
        val collection = userCollectionRepository.get(collectionId) ?: error("Could not find collection $collectionId")
        state.update { it.copy(collectionName = collection.name) }

        removals.setLoaded(repository.getByIds(collection.itemIds))
        renderBody()
    }

    /**
     * Rendering over a `Loading` or `Error` body would bury the pending fetch or the error message
     * under the previous read. [renderBody] cannot hold the guard itself: a load sets `Loading`
     * first and relies on its own fetch to render over it.
     */
    private fun renderBodyIfLoaded() {
        if (!state.value.isLoaded) return

        renderBody()
    }

    private fun renderBody() {
        val visible = removals.visible
        val body = if (visible.isEmpty()) {
            CollectionDetailState.Body.Empty
        } else {
            CollectionDetailState.Body.WithData(visible)
        }
        state.update { it.copy(body = body) }
    }
}
