package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddCreatureToListState
import com.cyrillrx.rpg.userlist.presentation.AddCreatureToListState.SelectableUserList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_creatures
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddCreatureToListViewModel(
    private val itemId: String,
    private val listType: UserList.Type,
    private val userListRepository: UserListRepository,
    private val creatureRepository: CreatureRepository,
) : ViewModel() {

    val state: StateFlow<AddCreatureToListState>
        field = MutableStateFlow(AddCreatureToListState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    init {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            state.update { it.copy(body = AddCreatureToListState.Body.Loading) }

            try {
                val creature = creatureRepository.getById(itemId) ?: error("Could not find creature $itemId")

                val userLists = userListRepository.getAll(listType)
                val selectableLists = userLists
                    .map { list -> SelectableUserList(list, alreadyAdded = itemId in list.itemIds) }
                val body = AddCreatureToListState.Body.WithData(
                    creature = creature,
                    selectableLists = selectableLists,
                )
                state.update { it.copy(body = body) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = AddCreatureToListState.Body.Error(errorMessage = Res.string.error_while_loading_creatures)) }
            }
        }
    }

    fun toggleSelection(listId: String) {
        state.update { state ->
            val body = state.body as? AddCreatureToListState.Body.WithData ?: return@update state

            val selectableLists = body.selectableLists.map { item ->
                if (item.list.id == listId) item.copy(isSelected = !item.isSelected) else item
            }

            state.copy(body = body.copy(selectableLists = selectableLists))
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

            state.update { state ->
                val body = state.body as? AddCreatureToListState.Body.WithData ?: return@update state

                val newLists = body.selectableLists + SelectableUserList(newList, alreadyAdded = true)
                state.copy(body = body.copy(selectableLists = newLists))
            }
        }
    }

    fun confirmSelection() {
        viewModelScope.launch {
            val body = state.value.body as? AddCreatureToListState.Body.WithData ?: return@launch

            body.selectableLists.forEach { selectableList -> selectableList.confirmSelection() }
            events.emit(Event.Dismiss)
        }
    }

    private suspend fun SelectableUserList.confirmSelection() {
        when {
            !alreadyAdded && isSelected -> userListRepository.addToList(list, itemId)
            alreadyAdded && !isSelected -> userListRepository.removeFromList(list, itemId)
        }
    }

    sealed class Event {
        data object Dismiss : Event()
    }
}
