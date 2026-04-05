package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.ListDetailState
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

    private var pendingRemoval: Pair<String, T>? = null

    init {
        loadDetail()
    }

    fun removeItemOptimistically(itemId: String, item: T) {
        pendingRemoval?.let { commitRemoval() }

        val currentState = state.value.body as? ListDetailState.Body.WithData ?: return

        pendingRemoval = Pair(itemId, item)
        val updatedItems = currentState.items - item
        val newBody = if (updatedItems.isEmpty()) {
            ListDetailState.Body.EmptyList
        } else {
            ListDetailState.Body.WithData(updatedItems)
        }
        state.update { it.copy(body = newBody) }
    }

    fun undoRemoval() {
        val (_, item) = pendingRemoval ?: return

        pendingRemoval = null
        val currentItems = when (val body = state.value.body) {
            is ListDetailState.Body.WithData -> body.items
            is ListDetailState.Body.EmptyList -> emptyList()
            else -> return
        }
        state.update { it.copy(body = ListDetailState.Body.WithData(currentItems + item)) }
    }

    fun commitRemoval() {
        val (itemId, _) = pendingRemoval ?: return
        pendingRemoval = null
        viewModelScope.launch {
            userListRepository.removeFromList(listId, itemId)
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
