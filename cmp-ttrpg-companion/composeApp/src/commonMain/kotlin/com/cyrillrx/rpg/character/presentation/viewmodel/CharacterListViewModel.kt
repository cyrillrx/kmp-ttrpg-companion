package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.CharacterSortOrder
import com.cyrillrx.rpg.character.domain.applySort
import com.cyrillrx.rpg.character.presentation.CharacterListState
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.presentation.OptimisticDeletions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    sealed interface Event {
        data class DeletionError(val character: Character) : Event
    }

    private val deletions = OptimisticDeletions<Stored<Character>> { it.value.id }

    /**
     * Detached from [viewModelScope] on purpose: a commit started when the snackbar expired must reach
     * the repository even if the user leaves the screen while the call is in flight.
     */
    private val commitScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var activeJob: Job? = null

    init {
        activeJob = loadCharacters(query = "")
    }

    fun filterByQuery(query: String) {
        activeJob?.cancel()
        activeJob = loadCharacters(query)
    }

    fun setSortOrder(order: CharacterSortOrder) {
        if (state.value.sortOrder == order) return

        state.update { current ->
            current.copy(
                sortOrder = order,
                body = if (current.body is CharacterListState.Body.WithData) bodyOf(order) else current.body,
            )
        }
    }

    fun silentRefresh() {
        if (state.value.body is CharacterListState.Body.Loading) return
        activeJob?.cancel()
        activeJob = refreshCharacters()
    }

    fun deleteCharacterOptimistically(stored: Stored<Character>): OptimisticDeletions.Pending<Stored<Character>>? {
        if (state.value.body !is CharacterListState.Body.WithData) return null

        val pending = deletions.hide(stored) ?: return null
        renderBody()
        return pending
    }

    fun undoDeletion(pending: OptimisticDeletions.Pending<Stored<Character>>) {
        if (!deletions.undo(pending)) return

        renderBodyIfLoaded()
    }

    fun commitDeletion(pending: OptimisticDeletions.Pending<Stored<Character>>) {
        if (!deletions.claim(pending)) return

        commit(pending)
    }

    internal fun commitAllPendingDeletions() {
        deletions.claimAll().forEach(::commit)
    }

    private fun commit(pending: OptimisticDeletions.Pending<Stored<Character>>) {
        commitScope.launch {
            val deleted = try {
                withContext(ioDispatcher) { repository.delete(pending.item.value.id) }
                true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                false
            }
            deletions.settle(pending, deleted)
            renderBodyIfLoaded()
            if (!deleted) events.emit(Event.DeletionError(pending.item.value))
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
            state.update { it.copy(searchQuery = query, body = CharacterListState.Body.Loading) }
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
        deletions.setLoaded(repository.getAll(filter))
        renderBody()
    }

    /**
     * Rendering over a `Loading` or `Error` body would bury the pending fetch or the error message
     * under the previous read. [renderBody] cannot hold the guard itself: a load sets `Loading`
     * first and relies on its own fetch to render over it.
     */
    private fun renderBodyIfLoaded() {
        val body = state.value.body
        if (body !is CharacterListState.Body.WithData && body !is CharacterListState.Body.Empty) return

        renderBody()
    }

    private fun renderBody() {
        state.update { it.copy(body = bodyOf(it.sortOrder)) }
    }

    /** Reads [OptimisticDeletions.visible] afresh, so it stays correct should [update] replay the lambda. */
    private fun bodyOf(order: CharacterSortOrder): CharacterListState.Body {
        val visible = deletions.visible
        return if (visible.isEmpty()) {
            CharacterListState.Body.Empty
        } else {
            CharacterListState.Body.WithData(visible.applySort(order))
        }
    }
}
