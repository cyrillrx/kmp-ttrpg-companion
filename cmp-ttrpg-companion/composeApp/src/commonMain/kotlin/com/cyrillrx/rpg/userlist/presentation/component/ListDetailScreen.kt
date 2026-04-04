package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.component.dialog.ConfirmDeleteDialog
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellItemProvider
import com.cyrillrx.rpg.userlist.presentation.ListDetailState
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.dialog_remove_from_list_message

@Composable
fun <T> ListDetailScreen(
    viewModel: ListDetailViewModel<T>,
    itemProvider: ListItemProvider<T>,
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ListDetailScreen(
        state = state,
        itemProvider = itemProvider,
        onNavigateUpClicked = onNavigateUp,
        onRemoveItemClicked = viewModel::removeItem,
    )
}

@Composable
fun <T> ListDetailScreen(
    state: ListDetailState<T>,
    itemProvider: ListItemProvider<T>,
    onNavigateUpClicked: () -> Unit,
    onRemoveItemClicked: (String) -> Unit,
) {
    var itemToRemove by remember { mutableStateOf<T?>(null) }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = state.listName,
                navigateUp = onNavigateUpClicked,
            )
        },
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
                    onRemoveItem = { itemToRemove = it },
                )
            }
        }
    }

    itemToRemove?.let { item ->
        ConfirmDeleteDialog(
            message = stringResource(Res.string.dialog_remove_from_list_message, itemProvider.getDisplayName(item)),
            onConfirm = {
                onRemoveItemClicked(itemProvider.getId(item))
                itemToRemove = null
            },
            onDismiss = { itemToRemove = null },
        )
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                uiProvider.ListItem(
                    entity = item,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { onRemoveItem(item) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
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
            itemProvider = SpellItemProvider(onItemClicked = {}),
            onNavigateUpClicked = {},
            onRemoveItemClicked = {},
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
            itemProvider = SpellItemProvider(onItemClicked = {}),
            onNavigateUpClicked = {},
            onRemoveItemClicked = {},
        )
    }
}
