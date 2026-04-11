package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.ListDetailState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_user_list
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

class ListDetailViewModel<T>(
    private val listId: String,
    private val userListRepository: UserListRepository,
    private val repository: EntityRepository<T>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val state: StateFlow<ListDetailState<T>>
        field = MutableStateFlow(ListDetailState())

    val events: SharedFlow<Event<T>>
        field = MutableSharedFlow<Event<T>>()

    data class PendingRemoval<T>(val itemId: String, val index: Int, val item: T)

    sealed interface Event<out T> {
        data class RemovalError<T>(val item: T) : Event<T>
    }

    private val pendingRemovals: MutableList<PendingRemoval<T>> = mutableListOf()
    private var currentList: UserList? = null

    init {
        loadDetail()
    }

    override fun onCleared() {
        super.onCleared()

        commitAllPendingRemovals()
    }

    fun renameList(newName: String) {
        val list = currentList ?: return

        viewModelScope.launch {
            val updatedList = list.copy(name = newName, lastModified = Clock.System.now())
            try {
                userListRepository.save(updatedList)
                currentList = updatedList
                state.update { it.copy(listName = newName) }
            } catch (e: Exception) {
                // TODO: Emit an event to notify UI about the error
            }
        }
    }

    fun removeItemOptimistically(itemId: String, item: T): PendingRemoval<T>? {
        val currentState = state.value.body as? ListDetailState.Body.WithData ?: return null

        val index = currentState.items.indexOf(item)
        if (index == -1) return null

        val pending = PendingRemoval(itemId, index, item)
        pendingRemovals.add(pending)
        val updatedItems = currentState.items - item
        val newBody = if (updatedItems.isEmpty()) {
            ListDetailState.Body.EmptyList
        } else {
            ListDetailState.Body.WithData(updatedItems)
        }
        state.update { it.copy(body = newBody) }
        return pending
    }

    fun undoRemoval(pending: PendingRemoval<T>) {
        if (!pendingRemovals.remove(pending)) return

        val currentItems = when (val body = state.value.body) {
            is ListDetailState.Body.WithData -> body.items
            is ListDetailState.Body.EmptyList -> emptyList()
            else -> return
        }
        val restoredItems = currentItems.toMutableList().apply { add(pending.index.coerceAtMost(size), pending.item) }
        state.update { it.copy(body = ListDetailState.Body.WithData(restoredItems)) }
    }

    fun commitRemoval(pending: PendingRemoval<T>) {
        if (!pendingRemovals.remove(pending)) return

        viewModelScope.launch {
            val result = userListRepository.removeFromList(listId, pending.itemId)
            if (result !is UserListRepository.Result.Success) {
                pendingRemovals.add(pending)
                undoRemoval(pending)
                events.emit(Event.RemovalError(pending.item))
            }
        }
    }

    internal fun commitAllPendingRemovals() {
        val toCommit = pendingRemovals.toList()
        pendingRemovals.clear()
        if (toCommit.isEmpty()) return

        CoroutineScope(ioDispatcher).launch {
            toCommit.forEach { pending ->
                userListRepository.removeFromList(listId, pending.itemId)
            }
        }
    }

    fun silentRefresh() {
        if (state.value.body is ListDetailState.Body.Loading) return

        viewModelScope.launch {
            try {
                fetchDetail()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Silently ignore — don't overwrite existing content with an error
            }
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            state.update { it.copy(body = ListDetailState.Body.Loading) }

            try {
                fetchDetail()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = ListDetailState.Body.Error(Res.string.error_while_loading_user_list)) }
            }
        }
    }

    private suspend fun fetchDetail() {
        val list = userListRepository.get(listId) ?: error("Could not find list $listId")
        currentList = list
        state.update { it.copy(listName = list.name) }

        val items = repository.getByIds(list.itemIds)
        val body = if (items.isEmpty()) {
            ListDetailState.Body.EmptyList
        } else {
            ListDetailState.Body.WithData(items)
        }
        state.update { it.copy(body = body) }
    }
}
