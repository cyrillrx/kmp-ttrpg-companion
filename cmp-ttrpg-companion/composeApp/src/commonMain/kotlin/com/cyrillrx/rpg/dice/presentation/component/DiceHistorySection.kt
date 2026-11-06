package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.EmptyStateLayout
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.rememberOptimisticDeleteHandler
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.sectionHeaderHeight
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceHistoryViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_clear_roll_history
import rpg_companion.composeapp.generated.resources.error_while_clearing_roll_history
import rpg_companion.composeapp.generated.resources.error_while_loading_roll_history
import rpg_companion.composeapp.generated.resources.message_no_previous_roll
import rpg_companion.composeapp.generated.resources.snackbar_roll_history_cleared
import rpg_companion.composeapp.generated.resources.title_roll_history

@Composable
fun DiceHistorySection(state: DiceHistoryState, onClearClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = sectionHeaderHeight),
        ) {
            Text(
                text = stringResource(Res.string.title_roll_history),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = spacingCommon),
            )
            ClearHistoryButton(state, onClearClicked)
        }

        RollHistoryBody(state.body, Res.string.message_no_previous_roll, Modifier.fillMaxSize())
    }
}

@Composable
fun ClearHistoryButton(state: DiceHistoryState, onClearClicked: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClearClicked, enabled = state.canClear, modifier = modifier) {
        Icon(Icons.Filled.Delete, contentDescription = stringResource(Res.string.btn_clear_roll_history))
    }
}

@Composable
fun RollHistoryBody(body: DiceHistoryState.Body, emptyMessage: StringResource, modifier: Modifier = Modifier) {
    when (body) {
        is DiceHistoryState.Body.Loading -> Loader(modifier)
        is DiceHistoryState.Body.Empty -> EmptyStateLayout(
            icon = Icons.Filled.History,
            message = stringResource(emptyMessage),
            modifier = modifier,
        )
        is DiceHistoryState.Body.Error -> ErrorLayout(Res.string.error_while_loading_roll_history, modifier)
        is DiceHistoryState.Body.WithData -> RollHistoryList(body, modifier)
    }
}

@Composable
fun rememberRollHistoryClear(viewModel: DiceHistoryViewModel, snackbarHostState: SnackbarHostState): () -> Unit {
    // TODO(#355): do not commit on a configuration change, the undo snackbar is still due
    DisposableEffect(viewModel) {
        onDispose { viewModel.commitPendingClears() }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                DiceHistoryViewModel.Event.ClearError -> launch {
                    snackbarHostState.showSnackbar(getString(Res.string.error_while_clearing_roll_history))
                }
            }
        }
    }

    val clearOptimistically = remember(viewModel) { { _: Unit -> viewModel.clearOptimistically() } }
    val onClear = rememberOptimisticDeleteHandler(
        snackbarHostState = snackbarHostState,
        onDeleteOptimistically = clearOptimistically,
        onUndo = viewModel::undoClear,
        onCommit = viewModel::commitClear,
        getMessage = { getString(Res.string.snackbar_roll_history_cleared) },
    )
    return remember(onClear) { { onClear(Unit) } }
}

@Preview
@Composable
private fun PreviewDiceHistorySectionLight() {
    AppThemePreview(darkTheme = false) { DiceHistorySection(DiceHistoryState(previewHistory), onClearClicked = {}) }
}

@Preview
@Composable
private fun PreviewDiceHistorySectionDark() {
    AppThemePreview(darkTheme = true) { DiceHistorySection(DiceHistoryState(previewHistory), onClearClicked = {}) }
}

@Preview
@Composable
private fun PreviewDiceHistorySectionEmpty() {
    AppThemePreview(darkTheme = true) {
        DiceHistorySection(DiceHistoryState(DiceHistoryState.Body.Empty), onClearClicked = {})
    }
}
