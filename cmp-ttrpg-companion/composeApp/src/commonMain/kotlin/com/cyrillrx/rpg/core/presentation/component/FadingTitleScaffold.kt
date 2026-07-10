package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.math.abs

/**
 * Scaffold for detail screens whose top-bar title fades in as the scrollable header name
 * scrolls up behind the bar: alpha ramps from 0 (no scroll) to 1 when the scroll offset
 * reaches the name's bottom, and fades back out on scroll up.
 *
 * @param content receives the [Modifier] to apply to the scrollable container ([scrollModifier])
 *   and the [Modifier] to attach to the header name whose bottom drives the fade ([titleModifier]).
 */
@Composable
fun FadingTitleScaffold(
    title: String,
    onNavigateUpClicked: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (scrollModifier: Modifier, titleModifier: Modifier) -> Unit,
) {
    val scrollState = rememberScrollState()
    var containerCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var titleBottomOffset by remember { mutableFloatStateOf(0f) }
    val titleAlpha by remember {
        derivedStateOf {
            if (titleBottomOffset <= 0f) 0f else (scrollState.value / titleBottomOffset).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SimpleTopBar(
                title = title,
                onNavigateUpClicked = onNavigateUpClicked,
                titleAlpha = titleAlpha,
                actions = actions,
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .onGloballyPositioned { containerCoordinates = it },
        ) {
            content(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                Modifier.onGloballyPositioned { coordinates ->
                    // Name bottom offset relative to the container, stable across scroll.
                    containerCoordinates?.let { container ->
                        val positionInContainer = container.localPositionOf(coordinates, Offset.Zero)
                        val newValue = positionInContainer.y + coordinates.size.height + scrollState.value
                        if (abs(titleBottomOffset - newValue) > 0.5f) {
                            titleBottomOffset = newValue
                        }
                    }
                },
            )
        }
    }
}
