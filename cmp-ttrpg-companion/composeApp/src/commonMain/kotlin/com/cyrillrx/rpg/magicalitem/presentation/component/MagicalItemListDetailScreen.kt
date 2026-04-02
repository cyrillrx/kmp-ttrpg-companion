package com.cyrillrx.rpg.magicalitem.presentation.component

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
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListDetailState
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListDetailViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.dialog_remove_from_list_message
import rpg_companion.composeapp.generated.resources.message_list_is_empty

@Composable
fun MagicalItemListDetailScreen(
    viewModel: MagicalItemListDetailViewModel,
    onNavigateUpClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MagicalItemListDetailScreen(
        state = state,
        onNavigateUpClicked = onNavigateUpClicked,
        onRemoveMagicalItemClicked = viewModel::removeMagicalItem,
    )
}

@Composable
fun MagicalItemListDetailScreen(
    state: MagicalItemListDetailState,
    onNavigateUpClicked: () -> Unit,
    onRemoveMagicalItemClicked: (String) -> Unit,
) {
    var itemToRemove by remember { mutableStateOf<MagicalItem?>(null) }

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
                is MagicalItemListDetailState.Body.Loading -> Loader()
                is MagicalItemListDetailState.Body.EmptyList -> ErrorLayout(Res.string.message_list_is_empty)
                is MagicalItemListDetailState.Body.Error -> ErrorLayout(body.errorMessage)
                is MagicalItemListDetailState.Body.WithData -> MagicalItemDetailList(
                    magicalItems = body.magicalItems,
                    onRemoveMagicalItem = { itemToRemove = it },
                )
            }
        }
    }

    itemToRemove?.let { magicalItem ->
        ConfirmDeleteDialog(
            message = stringResource(Res.string.dialog_remove_from_list_message, magicalItem.title),
            onConfirm = {
                onRemoveMagicalItemClicked(magicalItem.id)
                itemToRemove = null
            },
            onDismiss = { itemToRemove = null },
        )
    }
}

@Composable
private fun MagicalItemDetailList(
    magicalItems: List<MagicalItem>,
    onRemoveMagicalItem: (MagicalItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(magicalItems, key = { it.id }) { magicalItem ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                MagicalItemListItem(
                    magicalItem = magicalItem,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { onRemoveMagicalItem(magicalItem) }) {
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
private fun PreviewMagicalItemListDetailScreenLight() {
    MagicalItemListDetailScreenPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewMagicalItemListDetailScreenDark() {
    MagicalItemListDetailScreenPreview(darkTheme = true)
}

@Composable
private fun MagicalItemListDetailScreenPreview(darkTheme: Boolean) {
    val magicalItems = SampleMagicalItemRepository.getAll()
    AppThemePreview(darkTheme = darkTheme) {
        MagicalItemListDetailScreen(
            state = MagicalItemListDetailState(
                listName = "Loot",
                body = MagicalItemListDetailState.Body.WithData(magicalItems),
            ),
            onNavigateUpClicked = {},
            onRemoveMagicalItemClicked = {},
        )
    }
}
