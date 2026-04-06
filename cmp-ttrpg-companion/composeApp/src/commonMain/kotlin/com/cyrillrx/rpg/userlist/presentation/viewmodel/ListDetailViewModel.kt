package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.ListDetailState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_user_list
import kotlin.coroutines.cancellation.CancellationException

class ListDetailViewModel<T>(
    private val listId: String,
    private val userListRepository: UserListRepository,
    private val repository: EntityRepository<T>,
) : ViewModel() {

    val state: StateFlow<ListDetailState<T>>
        field = MutableStateFlow(ListDetailState())

    data class PendingRemoval<T>(val itemId: String, val index: Int, val item: T)

    private val pendingRemovals: MutableList<PendingRemoval<T>> = mutableListOf()

    init {
        loadDetail()
    }

    override fun onCleared() {
        super.onCleared()

        commitAllPendingRemovals()
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
                undoRemoval(pending)
            }
        }
    }

    internal fun commitAllPendingRemovals() {
        val toCommit = pendingRemovals.toList()
        pendingRemovals.clear()
        if (toCommit.isEmpty()) return

        CoroutineScope(Dispatchers.Main).launch {
            toCommit.forEach { pending ->
                userListRepository.removeFromList(listId, pending.itemId)
            }
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            state.update { it.copy(body = ListDetailState.Body.Loading) }

            try {
                val list = userListRepository.get(listId) ?: error("Could not find list $listId")
                state.update { it.copy(listName = list.name) }

                val items = repository.getByIds(list.itemIds)
                val body = if (items.isEmpty()) {
                    ListDetailState.Body.EmptyList
                } else {
                    ListDetailState.Body.WithData(items)
                }
                state.update { it.copy(body = body) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = ListDetailState.Body.Error(Res.string.error_while_loading_user_list)) }
            }
        }
    }
}
