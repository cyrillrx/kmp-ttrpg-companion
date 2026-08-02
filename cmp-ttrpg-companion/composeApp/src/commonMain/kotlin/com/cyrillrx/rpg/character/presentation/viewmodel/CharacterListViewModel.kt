package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.CharacterListState
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.presentation.commitAllPending
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_characters
import kotlin.coroutines.cancellation.CancellationException

class CharacterListViewModel(
    private val repository: CharacterRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val state: StateFlow<CharacterListState>
        field = MutableStateFlow(CharacterListState(searchQuery = "", body = CharacterListState.Body.Loading))

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    data class PendingDeletion(val stored: Stored<Character>, val index: Int)

    sealed interface Event {
        data class DeletionError(val character: Character) : Event
    }

    private val pendingDeletions: MutableList<PendingDeletion> = mutableListOf()
    private var activeJob: Job? = null

    init {
        loadCharacters(query = "")
    }

    fun filterByQuery(query: String) {
        activeJob?.cancel()
        activeJob = loadCharacters(query)
    }

    fun silentRefresh() {
        if (state.value.body is CharacterListState.Body.Loading) return
        activeJob?.cancel()
        activeJob = refreshCharacters()
    }

    fun deleteCharacterOptimistically(stored: Stored<Character>): PendingDeletion? {
        val currentState = state.value.body as? CharacterListState.Body.WithData ?: return null

        val index = currentState.searchResults.indexOf(stored)
        if (index == -1) return null

        val pending = PendingDeletion(stored, index)
        pendingDeletions.add(pending)
        val updatedList = currentState.searchResults - stored
        val newBody = if (updatedList.isEmpty()) {
            CharacterListState.Body.Empty
        } else {
            CharacterListState.Body.WithData(updatedList)
        }
        state.update { it.copy(body = newBody) }
        return pending
    }

    fun undoDeletion(pending: PendingDeletion) {
        if (!pendingDeletions.remove(pending)) return

        val currentList = when (val body = state.value.body) {
            is CharacterListState.Body.WithData -> body.searchResults
            is CharacterListState.Body.Empty -> emptyList()
            else -> return
        }
        val restoredList = currentList.toMutableList()
            .apply { add(pending.index.coerceAtMost(size), pending.stored) }
        state.update { it.copy(body = CharacterListState.Body.WithData(restoredList)) }
    }

    fun commitDeletion(pending: PendingDeletion) {
        if (!pendingDeletions.remove(pending)) return

        viewModelScope.launch {
            try {
                repository.delete(pending.stored.value.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                pendingDeletions.add(pending)
                undoDeletion(pending)
                events.emit(Event.DeletionError(pending.stored.value))
            }
        }
    }

    internal fun commitAllPendingDeletions() {
        pendingDeletions.commitAllPending(ioDispatcher) { pending ->
            repository.delete(pending.stored.value.id)
        }
    }

    private fun refreshCharacters(): Job =
        viewModelScope.launch {
            try {
                fetchAndUpdateCharacters(state.value.searchQuery)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Keep existing state on refresh failure
            }
        }

    private fun loadCharacters(query: String): Job =
        viewModelScope.launch {
            state.update { CharacterListState(searchQuery = query, body = CharacterListState.Body.Loading) }
            try {
                fetchAndUpdateCharacters(query)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        body = CharacterListState.Body.Error(errorMessage = Res.string.error_while_loading_characters),
                    )
                }
            }
        }

    private suspend fun fetchAndUpdateCharacters(query: String) {
        val filter = CharacterFilter(query = query)
        val characters = repository.getAll(filter)
        val body = if (characters.isEmpty()) {
            CharacterListState.Body.Empty
        } else {
            CharacterListState.Body.WithData(characters)
        }
        state.update { it.copy(body = body) }
    }
}
