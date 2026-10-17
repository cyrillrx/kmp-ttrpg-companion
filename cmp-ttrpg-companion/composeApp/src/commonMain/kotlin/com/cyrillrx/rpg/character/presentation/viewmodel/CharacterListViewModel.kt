package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.character.data.readCharacterExport
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.CharacterImportFailure
import com.cyrillrx.rpg.character.presentation.CharacterListState
import com.cyrillrx.rpg.character.presentation.toImportFailure
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.domain.StoredSortOrder
import com.cyrillrx.rpg.core.domain.applySort
import com.cyrillrx.rpg.core.presentation.OptimisticDeletions
import com.cyrillrx.rpg.core.presentation.detachedCommitScope
import com.cyrillrx.rpg.core.presentation.viewmodel.SearchableListViewModel
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_characters
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CharacterListViewModel(
    private val repository: CharacterRepository,
    private val prefsRepository: UserPreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val locale: String = currentLocale(),
) : SearchableListViewModel<CharacterListState, CharacterListState.Body>(
        initialState = CharacterListState(
            searchQuery = "",
            body = CharacterListState.Body.Loading,
            sortOrder = prefsRepository.preferences.value.characterSortOrder,
        ),
    ) {

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    sealed interface Event {
        data class DeletionError(val character: Character) : Event
        data class ImportSucceeded(val character: Character, val adjustmentCount: Int) : Event
        data class ImportFailed(val failure: CharacterImportFailure) : Event
    }

    private val deletions = OptimisticDeletions<Stored<Character>> { it.value.id }

    private val commitScope = detachedCommitScope()

    /** The query [deletions] was loaded with, which can lag behind the typed one while a search is pending. */
    private var loadedQuery = ""

    init {
        refresh()
    }

    fun filterByQuery(query: String) {
        val searchChanged = query.trim() != state.value.searchQuery.trim()
        val searchFailed = state.value.body is CharacterListState.Body.Error
        mutableState.update { it.copy(searchQuery = query) }
        if (searchChanged || searchFailed) refresh(debounce = true, resetScroll = true)
    }

    fun setSortOrder(order: StoredSortOrder) {
        if (state.value.sortOrder == order) return

        mutableState.update { current ->
            current.copy(
                sortOrder = order,
                body = if (showsContent(current.body)) sortedBody(order) else current.body,
            )
        }
        commitScope.launch {
            try {
                prefsRepository.setCharacterSortOrder(order)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // The chosen order stays applied for the session; only its persistence is lost.
                println("WARNING: failed to persist the character sort order: $e")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun importCharacter(content: String) {
        viewModelScope.launch {
            val event = try {
                when (val result = withContext(ioDispatcher) { readCharacterExport(content) }) {
                    is Result.Failure -> Event.ImportFailed(result.error.toImportFailure())
                    is Result.Success -> {
                        val character = result.value.value.copy(id = Uuid.random().toString())
                        withContext(ioDispatcher) { repository.save(character) }
                        Event.ImportSucceeded(character, adjustmentCount = result.value.warnings.size)
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Event.ImportFailed(CharacterImportFailure.Unexpected)
            }
            events.emit(event)
        }
    }

    fun reportUnreadableImport() {
        viewModelScope.launch { events.emit(Event.ImportFailed(CharacterImportFailure.Unexpected)) }
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

    /**
     * Rendering over a `Loading` or `Error` body would bury the pending fetch or the error message under the
     * previous read.
     */
    private fun renderBodyIfLoaded() {
        if (!showsContent(state.value.body)) return

        renderBody()
    }

    private fun renderBody() {
        mutableState.update { it.copy(body = sortedBody(it.sortOrder)) }
    }

    private fun sortedBody(order: StoredSortOrder): CharacterListState.Body {
        val visible = deletions.visible
        return when {
            visible.isNotEmpty() -> CharacterListState.Body.WithData(visible.applySort(order, locale))
            loadedQuery.isBlank() -> CharacterListState.Body.Empty
            else -> CharacterListState.Body.NoSearchResult(loadedQuery.trim())
        }
    }

    override fun CharacterListState.body(): CharacterListState.Body = body

    override fun CharacterListState.withBody(body: CharacterListState.Body): CharacterListState = copy(body = body)

    override fun loadingBody(): CharacterListState.Body = CharacterListState.Body.Loading

    override fun errorBody(): CharacterListState.Body =
        CharacterListState.Body.Error(errorMessage = Res.string.error_while_loading_characters)

    override fun showsContent(body: CharacterListState.Body): Boolean = when (body) {
        is CharacterListState.Body.Loading, is CharacterListState.Body.Error -> false
        is CharacterListState.Body.Empty,
        is CharacterListState.Body.NoSearchResult,
        is CharacterListState.Body.WithData,
        -> true
    }

    override suspend fun loadContent(): CharacterListState.Body {
        val query = state.value.searchQuery
        deletions.reload { repository.getAll(CharacterFilter(query = query)) }
        loadedQuery = query
        return sortedBody(state.value.sortOrder)
    }
}
