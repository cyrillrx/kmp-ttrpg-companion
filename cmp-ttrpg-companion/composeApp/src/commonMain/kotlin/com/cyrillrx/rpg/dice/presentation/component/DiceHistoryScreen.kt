package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.readingMaxWidth
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import com.cyrillrx.rpg.dice.presentation.navigation.DiceRouter
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceHistoryViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.title_roll_history

@Composable
fun DiceHistoryScreen(viewModel: DiceHistoryViewModel, router: DiceRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val onClearClicked = rememberRollHistoryClear(viewModel, snackbarHostState)

    DiceHistoryScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onNavigateUpClicked = router::navigateUp,
        onClearClicked = onClearClicked,
    )
}

@Composable
fun DiceHistoryScreen(
    state: DiceHistoryState,
    snackbarHostState: SnackbarHostState,
    onNavigateUpClicked: () -> Unit,
    onClearClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(titleResource = Res.string.title_roll_history, onNavigateUpClicked = onNavigateUpClicked) {
                ClearHistoryButton(state, onClearClicked)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            RollHistoryBody(
                body = state.body,
                modifier = Modifier
                    .widthIn(max = readingMaxWidth)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun DiceHistoryScreenPreview(state: DiceHistoryState) {
    DiceHistoryScreen(state, remember { SnackbarHostState() }, onNavigateUpClicked = {}, onClearClicked = {})
}

@Preview
@Composable
private fun PreviewDiceHistoryScreenLight() {
    AppThemePreview(darkTheme = false) { DiceHistoryScreenPreview(DiceHistoryState(previewHistory)) }
}

@Preview
@Composable
private fun PreviewDiceHistoryScreenDark() {
    AppThemePreview(darkTheme = true) { DiceHistoryScreenPreview(DiceHistoryState(previewHistory)) }
}

@Preview
@Composable
private fun PreviewDiceHistoryScreenEmpty() {
    AppThemePreview(darkTheme = true) { DiceHistoryScreenPreview(DiceHistoryState(DiceHistoryState.Body.Empty)) }
}
