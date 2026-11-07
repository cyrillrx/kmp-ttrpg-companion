package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun ScrollToTopOnEvent(events: Flow<Unit>, listState: LazyListState) {
    LaunchedEffect(events, listState) {
        events.collect { listState.scrollToItem(0) }
    }
}
