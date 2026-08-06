package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.AddToCollectionState
import com.cyrillrx.rpg.usercollection.presentation.AddToCollectionState.SelectableUserCollection
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddToCollectionViewModel<T>(
    private val listType: UserCollection.Type,
    private val userCollectionRepository: UserCollectionRepository,
    private val repository: EntityRepository<T>,
    private val errorMessage: StringResource,
) : ViewModel() {

    val state: StateFlow<AddToCollectionState<T>>
        field = MutableStateFlow(AddToCollectionState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    private var itemId: String = ""
    private var loadJob: Job? = null

    fun loadEntity(entityId: String) {
        itemId = entityId
        loadJob?.cancel()
        loadJob = viewModelScope.launch { loadLists() }
    }

    private suspend fun loadLists() {
        state.update { it.copy(body = AddToCollectionState.Body.Loading) }

        try {
            val item = repository.getById(itemId) ?: error("Could not find item $itemId")

            val userLists = userCollectionRepository.getAll(listType).sortedByDescending { it.updatedAt }
            val selectableCollections = userLists
                .map { stored -> SelectableUserCollection(stored, alreadyAdded = itemId in stored.value.itemIds) }
            state.update { it.copy(body = AddToCollectionState.Body.WithData(item, selectableCollections)) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            state.update { it.copy(body = AddToCollectionState.Body.Error(errorMessage)) }
        }
    }

    fun toggleSelection(listId: String) {
        state.update { state ->
            val body = state.body as? AddToCollectionState.Body.WithData ?: return@update state
            val updated = body.selectableCollections.map { item ->
                if (item.list.id == listId) item.copy(isSelected = !item.isSelected) else item
            }
            state.copy(body = body.copy(selectableCollections = updated))
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createAndAdd(name: String) {
        viewModelScope.launch {
            val newList = UserCollection(
                id = Uuid.random().toString(),
                name = name,
                type = listType,
                itemIds = listOf(itemId),
            )
            userCollectionRepository.save(newList)
            val stored = Stored(newList, Clock.System.now())

            state.update { state ->
                val body = state.body as? AddToCollectionState.Body.WithData ?: return@update state
                val newLists = body.selectableCollections + SelectableUserCollection(stored, alreadyAdded = true)
                state.copy(body = body.copy(selectableCollections = newLists))
            }
        }
    }

    fun confirmSelection() {
        viewModelScope.launch {
            val body = state.value.body as? AddToCollectionState.Body.WithData ?: return@launch
            body.selectableCollections.forEach { it.confirmSelection() }
            events.emit(Event.Dismiss)
        }
    }

    private suspend fun SelectableUserCollection.confirmSelection() {
        when {
            !alreadyAdded && isSelected -> userCollectionRepository.addToList(list, itemId)
            alreadyAdded && !isSelected -> userCollectionRepository.removeFromList(list, itemId)
        }
    }

    sealed class Event {
        data object Dismiss : Event()
    }
}
