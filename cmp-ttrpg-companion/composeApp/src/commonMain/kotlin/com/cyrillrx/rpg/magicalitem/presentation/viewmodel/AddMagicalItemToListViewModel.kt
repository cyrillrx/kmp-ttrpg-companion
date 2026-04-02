package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.AddMagicalItemToListState
import com.cyrillrx.rpg.magicalitem.presentation.AddMagicalItemToListState.SelectableUserList
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_magical_items
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddMagicalItemToListViewModel(
    private val itemId: String,
    private val listType: UserList.Type,
    private val userListRepository: UserListRepository,
    private val magicalItemRepository: MagicalItemRepository,
) : ViewModel() {

    val state: StateFlow<AddMagicalItemToListState>
        field = MutableStateFlow(AddMagicalItemToListState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    init {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            state.update { it.copy(body = AddMagicalItemToListState.Body.Loading) }

            try {
                val magicalItem = magicalItemRepository.getById(itemId) ?: error("Could not find magical item $itemId")

                val userLists = userListRepository.getAll(listType)
                val selectableLists = userLists
                    .map { list -> SelectableUserList(list, alreadyAdded = itemId in list.itemIds) }
                val body = AddMagicalItemToListState.Body.WithData(
                    magicalItem = magicalItem,
                    selectableLists = selectableLists,
                )
                state.update { it.copy(body = body) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = AddMagicalItemToListState.Body.Error(errorMessage = Res.string.error_while_loading_magical_items)) }
            }
        }
    }

    fun toggleSelection(listId: String) {
        state.update { state ->
            val body = state.body as? AddMagicalItemToListState.Body.WithData ?: return@update state

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
                val body = state.body as? AddMagicalItemToListState.Body.WithData ?: return@update state

                val newLists = body.selectableLists + SelectableUserList(newList, alreadyAdded = true)
                state.copy(body = body.copy(selectableLists = newLists))
            }
        }
    }

    fun confirmSelection() {
        viewModelScope.launch {
            val body = state.value.body as? AddMagicalItemToListState.Body.WithData ?: return@launch

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
        object Dismiss : Event()
    }
}
