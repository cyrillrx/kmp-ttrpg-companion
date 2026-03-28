package com.cyrillrx.rpg.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.presentation.AddToListState
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddToListViewModel(
    private val itemId: String,
    private val listType: UserList.Type,
    private val userListRepository: UserListRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AddToListState())
    val state: StateFlow<AddToListState> = _state.asStateFlow()

    init {
        loadLists()
    }

    fun addToList(listId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val list = userListRepository.get(listId) ?: return@launch
            if (itemId in list.itemIds) {
                onSuccess()
                return@launch
            }
            val updated = list.copy(itemIds = list.itemIds + itemId)
            userListRepository.save(updated)
            onSuccess()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createAndAdd(name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val newList = UserList(
                id = Uuid.random().toString(),
                name = name,
                type = listType,
                itemIds = listOf(itemId),
            )
            userListRepository.save(newList)
            onSuccess()
        }
    }

    private fun loadLists() {
        viewModelScope.launch {
            val lists = userListRepository.getAll(listType)
            _state.update { it.copy(body = AddToListState.Body.WithData(lists)) }
        }
    }
}
