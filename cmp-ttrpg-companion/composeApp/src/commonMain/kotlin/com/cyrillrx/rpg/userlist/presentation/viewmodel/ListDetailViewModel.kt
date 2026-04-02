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

    init {
        loadDetail()
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            val result = userListRepository.removeFromList(listId, itemId)
            if (result is UserListRepository.Result.Success) {
                loadDetail()
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
