package com.cyrillrx.rpg.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cyrillrx.rpg.core.presentation.ScrollPosition
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseListViewModel : ViewModel() {

    var savedScrollPosition: ScrollPosition = ScrollPosition()
        private set

    val scrollToTopEvents: SharedFlow<Unit>
        field = MutableSharedFlow(replay = 0)

    fun saveScrollPosition(position: ScrollPosition) {
        savedScrollPosition = position
    }

    protected fun scrollToTop() {
        savedScrollPosition = ScrollPosition()
        scrollToTopEvents.tryEmit(Unit)
    }
}
