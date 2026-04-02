package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListState
import com.cyrillrx.rpg.userlist.presentation.AddToListState.SelectableUserList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddToListViewModel<T>(
    private val itemId: String,
    private val listType: UserList.Type,
    private val userListRepository: UserListRepository,
    private val repository: EntityRepository<T>,
    private val errorMessage: StringResource,
) : ViewModel() {

    val state: StateFlow<AddToListState<T>>
        field = MutableStateFlow(AddToListState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    init {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            state.update { it.copy(body = AddToListState.Body.Loading) }

            try {
                val item = repository.getById(itemId) ?: error("Could not find item $itemId")

                val userLists = userListRepository.getAll(listType)
                val selectableLists = userLists
                    .map { list -> SelectableUserList(list, alreadyAdded = itemId in list.itemIds) }
                state.update { it.copy(body = AddToListState.Body.WithData(item, selectableLists)) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = AddToListState.Body.Error(errorMessage)) }
            }
        }
    }

    fun toggleSelection(listId: String) {
        state.update { state ->
            val body = state.body as? AddToListState.Body.WithData ?: return@update state
            val updated = body.selectableLists.map { item ->
                if (item.list.id == listId) item.copy(isSelected = !item.isSelected) else item
            }
            state.copy(body = body.copy(selectableLists = updated))
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
                val body = state.body as? AddToListState.Body.WithData ?: return@update state
                val newLists = body.selectableLists + SelectableUserList(newList, alreadyAdded = true)
                state.copy(body = body.copy(selectableLists = newLists))
            }
        }
    }

    fun confirmSelection() {
        viewModelScope.launch {
            val body = state.value.body as? AddToListState.Body.WithData ?: return@launch
            body.selectableLists.forEach { it.confirmSelection() }
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
