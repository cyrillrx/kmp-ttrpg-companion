package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellItemProvider
import com.cyrillrx.rpg.userlist.presentation.ListDetailState
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_undo
import rpg_companion.composeapp.generated.resources.snackbar_error_removing_from_list
import rpg_companion.composeapp.generated.resources.snackbar_removed_from_list

@Composable
fun <T> ListDetailScreen(
    viewModel: ListDetailViewModel<T>,
    itemProvider: ListItemProvider<T>,
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ListDetailScreen(
        state = state,
        events = viewModel.events,
        itemProvider = itemProvider,
        onNavigateUpClicked = onNavigateUp,
        onRemoveItemOptimistically = viewModel::removeItemOptimistically,
        onUndoRemoval = viewModel::undoRemoval,
        onCommitRemoval = viewModel::commitRemoval,
    )
}

@Composable
fun <T> ListDetailScreen(
    state: ListDetailState<T>,
    events: SharedFlow<ListDetailViewModel.Event<T>>,
    itemProvider: ListItemProvider<T>,
    onNavigateUpClicked: () -> Unit,
    onRemoveItemOptimistically: (id: String, item: T) -> ListDetailViewModel.PendingRemoval<T>?,
    onUndoRemoval: (ListDetailViewModel.PendingRemoval<T>) -> Unit,
    onCommitRemoval: (ListDetailViewModel.PendingRemoval<T>) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val undoLabel = stringResource(Res.string.btn_undo)

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is ListDetailViewModel.Event.RemovalError -> {
                    val displayName = itemProvider.getDisplayName(event.item)
                    val errorMessage = getString(Res.string.snackbar_error_removing_from_list, displayName)
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }

    fun onRemoveItem(item: T) {
        val pending = onRemoveItemOptimistically(itemProvider.getId(item), item) ?: return

        coroutineScope.launch {
            val displayName = itemProvider.getDisplayName(item)
            val removedMessage = getString(Res.string.snackbar_removed_from_list, displayName)
            val result = snackbarHostState.showSnackbar(
                message = removedMessage,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Long,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> onUndoRemoval(pending)
                SnackbarResult.Dismissed -> onCommitRemoval(pending)
            }
        }
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = state.listName,
                navigateUp = onNavigateUpClicked,
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
                is ListDetailState.Body.Loading -> Loader()
                is ListDetailState.Body.EmptyList -> itemProvider.EmptyLayout()
                is ListDetailState.Body.Error -> ErrorLayout(body.errorMessage)
                is ListDetailState.Body.WithData -> EntityDetailList(
                    items = body.items,
                    uiProvider = itemProvider,
                    onRemoveItem = ::onRemoveItem,
                )
            }
        }
    }
}

@Composable
private fun <T> EntityDetailList(
    items: List<T>,
    uiProvider: ListItemProvider<T>,
    onRemoveItem: (T) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(items, key = { uiProvider.getId(it) }) { item ->
            SwipeableListItem(
                item = item,
                uiProvider = uiProvider,
                onRemoveItem = onRemoveItem,
                modifier = Modifier.animateItem(),
            )
        }
    }
}

@Composable
private fun <T> SwipeableListItem(
    item: T,
    uiProvider: ListItemProvider<T>,
    onRemoveItem: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRemoveItem(item)
            }
            false
        },
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(end = spacingMedium),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        },
    ) {
        uiProvider.ListItem(
            entity = item,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewListDetailScreenLight() {
    ListDetailScreenPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewListDetailScreenDark() {
    ListDetailScreenPreview(darkTheme = true)
}

@Composable
private fun ListDetailScreenPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        ListDetailScreen(
            state = ListDetailState(
                listName = "Gandalf's Spells",
                body = ListDetailState.Body.WithData(SampleSpellRepository.getAll()),
            ),
            events = MutableSharedFlow(),
            itemProvider = SpellItemProvider(onItemClicked = {}),
            onNavigateUpClicked = {},
            onRemoveItemOptimistically = { _, _ -> null },
            onUndoRemoval = {},
            onCommitRemoval = {},
        )
    }
}

@Preview
@Composable
private fun PreviewEmptyListDetailScreenLight() {
    EmptyListDetailScreenPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewEmptyListDetailScreenDark() {
    EmptyListDetailScreenPreview(darkTheme = true)
}

@Composable
private fun EmptyListDetailScreenPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        ListDetailScreen(
            state = ListDetailState(
                listName = "Gandalf's Spells",
                body = ListDetailState.Body.EmptyList,
            ),
            events = MutableSharedFlow(),
            itemProvider = SpellItemProvider(onItemClicked = {}),
            onNavigateUpClicked = {},
            onRemoveItemOptimistically = { _, _ -> null },
            onUndoRemoval = {},
            onCommitRemoval = {},
        )
    }
}
