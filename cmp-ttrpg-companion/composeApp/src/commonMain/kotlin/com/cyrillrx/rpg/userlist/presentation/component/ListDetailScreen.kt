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
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.userlist.presentation.EntityUiProvider
import com.cyrillrx.rpg.userlist.presentation.ListDetailState
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.dialog_remove_from_list_message
import rpg_companion.composeapp.generated.resources.message_list_is_empty

@Composable
fun <T> ListDetailScreen(
    viewModel: ListDetailViewModel<T>,
    uiProvider: EntityUiProvider<T>,
    onNavigateUpClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ListDetailScreen(
        state = state,
        uiProvider = uiProvider,
        onNavigateUpClicked = onNavigateUpClicked,
        onRemoveItemClicked = viewModel::removeItem,
    )
}

@Composable
fun <T> ListDetailScreen(
    state: ListDetailState<T>,
    uiProvider: EntityUiProvider<T>,
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
                is ListDetailState.Body.EmptyList -> ErrorLayout(Res.string.message_list_is_empty)
                is ListDetailState.Body.Error -> ErrorLayout(body.errorMessage)
                is ListDetailState.Body.WithData -> EntityDetailList(
                    items = body.items,
                    uiProvider = uiProvider,
                    onRemoveItem = { itemToRemove = it },
                )
            }
        }
    }

    itemToRemove?.let { item ->
        ConfirmDeleteDialog(
            message = stringResource(Res.string.dialog_remove_from_list_message, uiProvider.getDisplayName(item)),
            onConfirm = {
                onRemoveItemClicked(uiProvider.getId(item))
                itemToRemove = null
            },
            onDismiss = { itemToRemove = null },
        )
    }
}

@Composable
private fun <T> EntityDetailList(
    items: List<T>,
    uiProvider: EntityUiProvider<T>,
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
