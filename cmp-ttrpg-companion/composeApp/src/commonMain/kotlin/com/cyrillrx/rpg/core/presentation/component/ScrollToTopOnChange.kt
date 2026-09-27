package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * A bare `LaunchedEffect(key)` also runs when the list re-enters composition, e.g. on back navigation,
 * and would reset the scroll position the back stack just restored. Remembering the last [key] across
 * that round trip lets the scroll follow a real change only.
 */
@Composable
fun ScrollToTopOnChange(key: Any, listState: LazyListState) {
    var lastKey by rememberSaveable { mutableStateOf(key) }

    LaunchedEffect(key) {
        if (key == lastKey) return@LaunchedEffect

        lastKey = key
        listState.animateScrollToItem(0)
    }
}
