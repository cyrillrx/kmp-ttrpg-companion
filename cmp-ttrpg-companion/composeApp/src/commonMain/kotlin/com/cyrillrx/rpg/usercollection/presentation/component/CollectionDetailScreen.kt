package com.cyrillrx.rpg.usercollection.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.component.SwipeToDelete
import com.cyrillrx.rpg.core.presentation.component.dialog.RenameCollectionDialog
import com.cyrillrx.rpg.core.presentation.component.rememberOptimisticDeleteHandler
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellItemProvider
import com.cyrillrx.rpg.usercollection.presentation.CollectionDetailState
import com.cyrillrx.rpg.usercollection.presentation.CollectionItemProvider
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.CollectionDetailViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_rename_collection
import rpg_companion.composeapp.generated.resources.snackbar_error_removing_from_collection
import rpg_companion.composeapp.generated.resources.snackbar_removed_from_collection

@Composable
fun <T> CollectionDetailScreen(
    viewModel: CollectionDetailViewModel<T>,
    itemProvider: CollectionItemProvider<T>,
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.silentRefresh()
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.commitAllPendingRemovals() }
    }

    CollectionDetailScreen(
        state = state,
        events = viewModel.events,
        itemProvider = itemProvider,
        onNavigateUpClicked = onNavigateUp,
        onRenameCollection = viewModel::renameCollection,
        onRemoveItemOptimistically = viewModel::removeItemOptimistically,
        onUndoRemoval = viewModel::undoRemoval,
        onCommitRemoval = viewModel::commitRemoval,
    )
}

@Composable
fun <T> CollectionDetailScreen(
    state: CollectionDetailState<T>,
    events: SharedFlow<CollectionDetailViewModel.Event<T>>,
    itemProvider: CollectionItemProvider<T>,
    onNavigateUpClicked: () -> Unit,
    onRenameCollection: (String) -> Unit,
    onRemoveItemOptimistically: (id: String, item: T) -> CollectionDetailViewModel.PendingRemoval<T>?,
    onUndoRemoval: (CollectionDetailViewModel.PendingRemoval<T>) -> Unit,
    onCommitRemoval: (CollectionDetailViewModel.PendingRemoval<T>) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showRenameDialog by remember { mutableStateOf(false) }

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is CollectionDetailViewModel.Event.RemovalError -> {
                    val displayName = itemProvider.getDisplayName(event.item, currentLocale())
                    val errorMessage = getString(Res.string.snackbar_error_removing_from_collection, displayName)
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }

    val onRemoveItem = rememberOptimisticDeleteHandler<T, CollectionDetailViewModel.PendingRemoval<T>>(
        snackbarHostState = snackbarHostState,
        onDeleteOptimistically = { item -> onRemoveItemOptimistically(itemProvider.getId(item), item) },
        onUndo = onUndoRemoval,
        onCommit = onCommitRemoval,
        getMessage = { item ->
            val displayName = itemProvider.getDisplayName(item, currentLocale())
            getString(Res.string.snackbar_removed_from_collection, displayName)
        },
    )

    if (showRenameDialog) {
        RenameCollectionDialog(
            currentName = state.collectionName,
            onConfirm = { newName ->
                onRenameCollection(newName)
                showRenameDialog = false
            },
            onDismiss = { showRenameDialog = false },
        )
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = state.collectionName,
                onNavigateUpClicked = onNavigateUpClicked,
                actions = {
                    IconButton(onClick = { showRenameDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(Res.string.btn_rename_collection))
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val body = state.body) {
                is CollectionDetailState.Body.Loading -> Loader()
                is CollectionDetailState.Body.Empty -> itemProvider.EmptyLayout()
                is CollectionDetailState.Body.Error -> ErrorLayout(body.errorMessage)
                is CollectionDetailState.Body.WithData -> EntityDetailList(
                    items = body.items,
                    uiProvider = itemProvider,
                    onRemoveItem = onRemoveItem,
                )
            }
        }
    }
}

@Composable
private fun <T> EntityDetailList(
    items: List<T>,
    uiProvider: CollectionItemProvider<T>,
    onRemoveItem: (T) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
    ) {
        items(items, key = { uiProvider.getId(it) }) { item ->
            SwipeToDelete(
                onSwiped = { onRemoveItem(item) },
                modifier = Modifier.fillMaxWidth().animateItem(),
            ) {
                uiProvider.ListItem(entity = item, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCollectionDetailScreenLight() {
    CollectionDetailScreenPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewCollectionDetailScreenDark() {
    CollectionDetailScreenPreview(darkTheme = true)
}

@Composable
private fun CollectionDetailScreenPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        CollectionDetailScreen(
            state = CollectionDetailState(
                collectionName = "Gandalf's Spells",
                body = CollectionDetailState.Body.WithData(SampleSpellRepository.getAll()),
            ),
            events = MutableSharedFlow(),
            itemProvider = SpellItemProvider(onItemClicked = {}),
            onNavigateUpClicked = {},
            onRenameCollection = {},
            onRemoveItemOptimistically = { _, _ -> null },
            onUndoRemoval = {},
            onCommitRemoval = {},
        )
    }
}

@Preview
@Composable
private fun PreviewEmptyCollectionDetailScreenLight() {
    EmptyCollectionDetailScreenPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewEmptyCollectionDetailScreenDark() {
    EmptyCollectionDetailScreenPreview(darkTheme = true)
}

@Composable
private fun EmptyCollectionDetailScreenPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        CollectionDetailScreen(
            state = CollectionDetailState(
                collectionName = "Gandalf's Spells",
                body = CollectionDetailState.Body.Empty,
            ),
            events = MutableSharedFlow(),
            itemProvider = SpellItemProvider(onItemClicked = {}),
            onNavigateUpClicked = {},
            onRenameCollection = {},
            onRemoveItemOptimistically = { _, _ -> null },
            onUndoRemoval = {},
            onCommitRemoval = {},
        )
    }
}
