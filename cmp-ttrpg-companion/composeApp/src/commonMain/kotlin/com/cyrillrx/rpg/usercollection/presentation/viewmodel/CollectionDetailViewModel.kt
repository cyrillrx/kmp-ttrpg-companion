package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.core.presentation.commitAllPending
import com.cyrillrx.rpg.usercollection.domain.UserCollection
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

    data class PendingRemoval<T>(val itemId: String, val index: Int, val item: T)

    sealed interface Event<out T> {
        data class RemovalError<T>(val item: T) : Event<T>
        data object RenameError : Event<Nothing>
    }

    private val pendingRemovals: MutableList<PendingRemoval<T>> = mutableListOf()
    private var currentCollection: UserCollection? = null
    private var activeJob: Job? = null

    init {
        activeJob = loadDetail()
    }

    fun renameCollection(newName: String) {
        viewModelScope.launch {
            try {
                val result = userCollectionRepository.rename(collectionId, newName)
                if (result is UserCollectionRepository.Result.Success) {
                    currentCollection = currentCollection?.copy(name = newName)
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
        val currentState = state.value.body as? CollectionDetailState.Body.WithData ?: return null

        val index = currentState.items.indexOf(item)
        if (index == -1) return null

        val pending = PendingRemoval(itemId, index, item)
        pendingRemovals.add(pending)
        val updatedItems = currentState.items - item
        val newBody = if (updatedItems.isEmpty()) {
            CollectionDetailState.Body.Empty
        } else {
            CollectionDetailState.Body.WithData(updatedItems)
        }
        state.update { it.copy(body = newBody) }
        return pending
    }

    fun undoRemoval(pending: PendingRemoval<T>) {
        if (!pendingRemovals.remove(pending)) return

        val currentItems = when (val body = state.value.body) {
            is CollectionDetailState.Body.WithData -> body.items
            is CollectionDetailState.Body.Empty -> emptyList()
            else -> return
        }
        val restoredItems = currentItems.toMutableList().apply { add(pending.index.coerceAtMost(size), pending.item) }
        state.update { it.copy(body = CollectionDetailState.Body.WithData(restoredItems)) }
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
            if (result !is UserCollectionRepository.Result.Success) {
                pendingRemovals.add(pending)
                undoRemoval(pending)
                events.emit(Event.RemovalError(pending.item))
            }
        }
    }

    internal fun commitAllPendingRemovals() {
        pendingRemovals.commitAllPending(ioDispatcher) { pending ->
            userCollectionRepository.removeFromCollection(collectionId, pending.itemId)
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
        currentCollection = collection
        state.update { it.copy(collectionName = collection.name) }

        val items = repository.getByIds(collection.itemIds)
        val body = if (items.isEmpty()) {
            CollectionDetailState.Body.Empty
        } else {
            CollectionDetailState.Body.WithData(items)
        }
        state.update { it.copy(body = body) }
    }
}
