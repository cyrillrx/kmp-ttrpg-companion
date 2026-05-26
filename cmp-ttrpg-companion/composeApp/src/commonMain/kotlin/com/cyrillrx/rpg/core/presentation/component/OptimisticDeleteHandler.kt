package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_undo

@Composable
fun <Item, Pending> rememberOptimisticDeleteHandler(
    snackbarHostState: SnackbarHostState,
    onDeleteOptimistically: (Item) -> Pending?,
    onUndo: (Pending) -> Unit,
    onCommit: (Pending) -> Unit,
    getMessage: suspend (Item) -> String,
): (Item) -> Unit {
    val coroutineScope = rememberCoroutineScope()
    val undoLabel = stringResource(Res.string.btn_undo)
    return remember(onDeleteOptimistically, onUndo, onCommit) {
        handler@{ item ->
            val pending = onDeleteOptimistically(item) ?: return@handler

            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = getMessage(item),
                    actionLabel = undoLabel,
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> onUndo(pending)
                    SnackbarResult.Dismissed -> onCommit(pending)
                }
            }
        }
    }
}
