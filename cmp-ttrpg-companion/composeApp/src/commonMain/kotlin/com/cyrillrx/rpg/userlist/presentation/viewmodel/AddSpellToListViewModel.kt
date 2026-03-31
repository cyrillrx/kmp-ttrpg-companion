package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_spells
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddSpellToListViewModel(
    private val itemId: String,
    private val listType: UserList.Type,
    private val userListRepository: UserListRepository,
    private val spellRepository: SpellRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AddToListState())
    val state: StateFlow<AddToListState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    init {
        loadLists()
    }

    fun toggleSelection(listId: String) {
        _state.update { state ->
            val body = state.body as? AddToListState.Body.WithData ?: return@update state
            val newSelection = if (listId in body.pendingSelection)
                body.pendingSelection - listId
            else
                body.pendingSelection + listId
            state.copy(body = body.copy(pendingSelection = newSelection))
        }
    }

    fun confirmSelection() {
        viewModelScope.launch {
            val body = _state.value.body as? AddToListState.Body.WithData ?: return@launch
            body.lists.forEach { item ->
                val wasAdded = item.alreadyAdded
                val isSelected = item.list.id in body.pendingSelection
                when {
                    !wasAdded && isSelected -> {
                        val list = userListRepository.get(item.list.id) ?: return@forEach
                        userListRepository.save(list.copy(itemIds = list.itemIds + itemId))
                    }

                    wasAdded && !isSelected -> {
                        val list = userListRepository.get(item.list.id) ?: return@forEach
                        userListRepository.save(list.copy(itemIds = list.itemIds - itemId))
                    }
                }
            }
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
            _state.update { it.copy(body = AddToListState.Body.Loading) }

            try {
                val spell = spellRepository.getById(itemId) ?: error("Could Not find spell $itemId")

                val lists = userListRepository.getAll(listType)
                val selectableLists = lists.map { list ->
                    AddToListState.SelectableUserList(list, alreadyAdded = itemId in list.itemIds)
                }
                val initialSelection = selectableLists.filter { it.alreadyAdded }.map { it.list.id }.toSet()

                val body = AddToListState.Body.WithData(
                    spell = spell,
                    lists = selectableLists,
                    pendingSelection = initialSelection,
                )
                _state.update { it.copy(body = body) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(body = AddToListState.Body.Error(errorMessage = Res.string.error_while_loading_spells)) }
            }
        }
    }

    sealed class Event {
        object Dismiss : Event()
    }
}
