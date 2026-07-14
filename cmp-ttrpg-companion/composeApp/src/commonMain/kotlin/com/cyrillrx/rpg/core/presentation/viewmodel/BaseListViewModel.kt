package com.cyrillrx.rpg.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cyrillrx.rpg.core.presentation.ScrollPosition
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/** Delay before re-filtering after a search-query change, so typing doesn't filter on every keystroke. */
internal const val SEARCH_DEBOUNCE_MS = 250L

abstract class BaseListViewModel : ViewModel() {

    var savedScrollPosition: ScrollPosition = ScrollPosition()
        private set

    val scrollToTopEvents: SharedFlow<Unit>
        field = MutableSharedFlow(replay = 0, extraBufferCapacity = 1)

    fun saveScrollPosition(position: ScrollPosition) {
        savedScrollPosition = position
    }

    protected fun scrollToTop() {
        savedScrollPosition = ScrollPosition()
        scrollToTopEvents.tryEmit(Unit)
    }
}
