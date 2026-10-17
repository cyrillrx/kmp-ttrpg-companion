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
 * Base view model for filterable/searchable lists: owns the cancellable load job, query debouncing,
 * scroll-reset timing and the no-loader-flicker guard shared by every compendium list.
 */
abstract class SearchableListViewModel<State : Any, Body : Any>(
    initialState: State,
) : BaseListViewModel() {

    protected val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()

    private var updateJob: Job? = null
    private var updateJobIsSilent = false
    private var silentRefreshQueued = false

    protected abstract fun State.body(): Body
    protected abstract fun State.withBody(body: Body): State

    protected abstract fun loadingBody(): Body

    protected abstract fun errorBody(): Body

    /** Whether [body] already shows content that can stay visible during a refilter, so no loader flashes. */
    protected abstract fun showsContent(body: Body): Boolean

    protected abstract suspend fun loadContent(): Body

    /**
     * Reloads with the current filter, keeping what is shown if the read fails. A pending silent refresh is
     * restarted so the latest data wins; any other pending load is left to finish, then refreshed again, as
     * its read may predate the change this refresh is for.
     */
    fun silentRefresh() {
        if (updateJob?.isActive == true && !updateJobIsSilent) {
            silentRefreshQueued = true
            return
        }

        refresh(silent = true)
    }

    protected fun refresh(debounce: Boolean = false, resetScroll: Boolean = false, silent: Boolean = false) {
        updateJob?.cancel()
        updateJobIsSilent = silent
        silentRefreshQueued = false
        updateJob = viewModelScope.launch {
            if (debounce) delay(SEARCH_DEBOUNCE_MS)
            if (resetScroll) scrollToTop()

            if (!silent && !showsContent(mutableState.value.body())) {
                mutableState.update { it.withBody(loadingBody()) }
            }

            val body = try {
                loadContent()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (silent) {
                    // The shown content stays; only the refresh is lost.
                    println("WARNING: silent list refresh failed: $e")
                    return@launch
                }
                errorBody()
            }
            mutableState.update { it.withBody(body) }
            if (silentRefreshQueued) refresh(silent = true)
        }
    }
}
