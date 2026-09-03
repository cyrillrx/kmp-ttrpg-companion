package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.core.presentation.commitAllPending
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.CollectionDetailState
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
import rpg_companion.composeapp.generated.resources.error_while_loading_collection
import kotlin.coroutines.cancellation.CancellationException

class CollectionDetailViewModel<T>(
    private val collectionId: String,
    private val userCollectionRepository: UserCollectionRepository,
    private val repository: EntityRepository<T>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val state: StateFlow<CollectionDetailState<T>>
        field = MutableStateFlow(CollectionDetailState())

    val events: SharedFlow<Event<T>>
        field = MutableSharedFlow<Event<T>>()

    data class PendingRemoval<T>(val itemId: String, val item: T)

    sealed interface Event<out T> {
        data class RemovalError<T>(val item: T) : Event<T>
        data object RenameError : Event<Nothing>
    }

    private val pendingRemovals: MutableList<PendingRemoval<T>> = mutableListOf()

    /**
     * Last list read from the repository. The rendered body is derived from it by subtracting the
     * pending removals, so a refresh landing inside the undo window cannot resurrect a swiped item.
     */
    private var loadedItems: List<T> = emptyList()
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

    fun removeItemOptimistically(itemId: String, item: T): PendingRemoval<T>? {
        val body = state.value.body as? CollectionDetailState.Body.WithData ?: return null
        if (item !in body.items) return null

        val pending = PendingRemoval(itemId, item)
        pendingRemovals.add(pending)
        renderBody()
        return pending
    }

    fun undoRemoval(pending: PendingRemoval<T>) {
        if (!pendingRemovals.remove(pending)) return

        renderBodyIfLoaded()
    }

    fun commitRemoval(pending: PendingRemoval<T>) {
        if (!pendingRemovals.remove(pending)) return

        viewModelScope.launch {
            val result = try {
                userCollectionRepository.removeFromCollection(collectionId, pending.itemId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                UserCollectionRepository.Result.Error(e.message ?: "removal failed")
            }
            val removed = result is UserCollectionRepository.Result.Success
            if (removed) loadedItems = loadedItems - pending.item
            renderBodyIfLoaded()
            if (!removed) events.emit(Event.RemovalError(pending.item))
        }
    }

    internal fun commitAllPendingRemovals() {
        val committedItems = pendingRemovals.map { it.item }
        pendingRemovals.commitAllPending(ioDispatcher) { pending ->
            userCollectionRepository.removeFromCollection(collectionId, pending.itemId)
        }
        loadedItems = loadedItems - committedItems
        renderBodyIfLoaded()
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

        loadedItems = repository.getByIds(collection.itemIds)
        renderBody()
    }

    /**
     * Re-derives the body only when one is already displayed. A `Loading` or `Error` body means a
     * fetch is in flight or has failed, and rendering there would cover it with the previous read,
     * hiding the pending load or the error message behind stale data. [renderBody] cannot hold the
     * guard itself: a load sets `Loading` first and relies on its own fetch to render over it.
     */
    private fun renderBodyIfLoaded() {
        if (!state.value.isLoaded) return

        renderBody()
    }

    private fun renderBody() {
        val hiddenItems = pendingRemovals.map { it.item }
        val visibleItems = loadedItems.filterNot { it in hiddenItems }
        val body = if (visibleItems.isEmpty()) {
            CollectionDetailState.Body.Empty
        } else {
            CollectionDetailState.Body.WithData(visibleItems)
        }
        state.update { it.copy(body = body) }
    }
}
