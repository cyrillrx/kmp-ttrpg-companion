package com.cyrillrx.rpg.core.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/** Delay before re-filtering after a search-query change, so typing doesn't filter on every keystroke. */
internal const val SEARCH_DEBOUNCE_MS = 250L

/**
 * Base view model for filterable/searchable lists. Owns the load lifecycle shared by every
 * compendium list: a single cancellable [updateJob], search-query debouncing, scroll reset timing
 * and the "keep current results visible while re-filtering" behavior. Subclasses only describe how
 * to read/write their own [State]/[Body] and how to fetch results for the current filter.
 */
abstract class SearchableListViewModel<State : Any, Body : Any>(
    initialState: State,
) : BaseListViewModel() {

    protected val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()

    private var updateJob: Job? = null

    protected abstract fun State.body(): Body
    protected abstract fun State.withBody(body: Body): State

    /** Body shown while the initial (or a post-error) load is running. */
    protected abstract fun loadingBody(): Body

    /** Body shown when [loadContent] fails. */
    protected abstract fun errorBody(): Body

    /**
     * Whether [body] already shows something the user can keep looking at (results or an empty
     * result) while a new load runs. When true, no loader is shown, avoiding a list → loader → list
     * flicker on every re-filter.
     */
    protected abstract fun showsContent(body: Body): Boolean

    /** Fetches the results for the current filter and maps them to a display body. */
    protected abstract suspend fun loadContent(): Body

    /**
     * Cancels any in-flight load and (re)loads the list.
     *
     * @param debounce collapses a burst of query changes into a single load once the user pauses.
     * @param resetScroll scrolls the list back to top on the same (debounced) path, so the scroll
     *   reset stays in sync with the results instead of firing on every keystroke.
     */
    protected fun refresh(debounce: Boolean = false, resetScroll: Boolean = false) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            if (debounce) delay(SEARCH_DEBOUNCE_MS)
            if (resetScroll) scrollToTop()

            if (!showsContent(mutableState.value.body())) {
                mutableState.update { it.withBody(loadingBody()) }
            }

            val body = try {
                loadContent()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                errorBody()
            }
            mutableState.update { it.withBody(body) }
        }
    }
}
