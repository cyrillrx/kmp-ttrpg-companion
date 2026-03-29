package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.presentation.AddToListState
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    init {
        loadLists()
    }

    fun addToList(listId: String) {
        viewModelScope.launch {
            val list = userListRepository.get(listId) ?: return@launch
            if (itemId in list.itemIds) {
                _events.emit(Event.Dismiss)
                return@launch
            }
            val updated = list.copy(itemIds = list.itemIds + itemId)
            userListRepository.save(updated)
            _events.emit(Event.Dismiss)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createAndAdd(name: String) {
        viewModelScope.launch {
            val newList = UserList(
                id = Uuid.random().toString(),
                name = name,
                type = listType,
                itemIds = listOf(itemId),
            )
            userListRepository.save(newList)
            _events.emit(Event.Dismiss)
        }
    }

    private fun loadLists() {
        viewModelScope.launch {
            val lists = userListRepository.getAll(listType)
            _state.update { it.copy(body = AddToListState.Body.WithData(lists)) }
        }
    }

    sealed class Event {
        object Dismiss : Event()
    }
}
