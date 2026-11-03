package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.EmptyStateLayout
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.component.rememberOptimisticDeleteHandler
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.contentMaxWidth
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import com.cyrillrx.rpg.dice.presentation.navigation.DiceRouter
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceHistoryViewModel
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_clear_roll_history
import rpg_companion.composeapp.generated.resources.error_while_clearing_roll_history
import rpg_companion.composeapp.generated.resources.error_while_loading_roll_history
import rpg_companion.composeapp.generated.resources.message_no_roll_yet
import rpg_companion.composeapp.generated.resources.snackbar_roll_history_cleared
import rpg_companion.composeapp.generated.resources.title_roll_history

@Composable
fun DiceHistoryScreen(viewModel: DiceHistoryViewModel, router: DiceRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(viewModel) {
        onDispose { viewModel.commitPendingClears() }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                DiceHistoryViewModel.Event.ClearError ->
                    snackbarHostState.showSnackbar(getString(Res.string.error_while_clearing_roll_history))
            }
        }
    }

    val clearOptimistically = remember(viewModel) { { _: Unit -> viewModel.clearOptimistically() } }
    val onClearClicked = rememberOptimisticDeleteHandler(
        snackbarHostState = snackbarHostState,
        onDeleteOptimistically = clearOptimistically,
        onUndo = viewModel::undoClear,
        onCommit = viewModel::commitClear,
        getMessage = { getString(Res.string.snackbar_roll_history_cleared) },
    )

    DiceHistoryScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onNavigateUpClicked = router::navigateUp,
        onClearClicked = { onClearClicked(Unit) },
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
                IconButton(onClick = onClearClicked, enabled = state.body is DiceHistoryState.Body.WithData) {
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(Res.string.btn_clear_roll_history))
                }
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
            val contentModifier = Modifier
                .widthIn(max = contentMaxWidth)
                .fillMaxSize()
            when (val body = state.body) {
                is DiceHistoryState.Body.Loading -> Loader(contentModifier)
                is DiceHistoryState.Body.Empty -> EmptyStateLayout(
                    icon = Icons.Filled.History,
                    message = stringResource(Res.string.message_no_roll_yet),
                    modifier = contentModifier,
                )
                is DiceHistoryState.Body.Error -> ErrorLayout(
                    Res.string.error_while_loading_roll_history,
                    contentModifier,
                )
                is DiceHistoryState.Body.WithData -> RollHistoryList(body, contentModifier)
            }
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
