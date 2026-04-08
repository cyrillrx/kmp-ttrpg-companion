package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.dialog.CreateListDialog
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellAddToListProvider
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListState
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddToListViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.btn_create_list
import rpg_companion.composeapp.generated.resources.title_save_to_list

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AddToListBottomSheet(
    viewModel: AddToListViewModel<T>,
    header: @Composable (T) -> Unit,
    onDismiss: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.events.collect {
            when (it) {
                AddToListViewModel.Event.Dismiss -> onDismiss()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        AddToListBottomSheetContent(
            body = state.body,
            header = header,
            onToggleSelection = viewModel::toggleSelection,
            onConfirm = viewModel::confirmSelection,
            onCreateListClicked = { showCreateDialog = true },
        )
    }

    if (showCreateDialog) {
        CreateListDialog(
            onConfirm = { name ->
                viewModel.createAndAdd(name)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false },
        )
    }
}

@Composable
private fun <T> ColumnScope.AddToListBottomSheetContent(
    body: AddToListState.Body<T>,
    header: @Composable (T) -> Unit,
    onToggleSelection: (String) -> Unit,
    onConfirm: () -> Unit,
    onCreateListClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.weight(1f, fill = false),
        contentPadding = PaddingValues(
            start = spacingMedium,
            end = spacingMedium,
            top = spacingCommon,
            bottom = spacingCommon,
        ),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        when (body) {
            is AddToListState.Body.Loading -> item { Loader() }
            is AddToListState.Body.Error -> item { ErrorLayout(body.errorMessage) }
            is AddToListState.Body.WithData -> {
                item { header(body.item) }
                item {
                    Text(
                        text = stringResource(Res.string.title_save_to_list),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(spacingMedium),
                    )
                }
                items(body.selectableLists, key = { it.list.id }) { item ->
                    SelectableUserListItem(
                        name = item.list.name,
                        isSelected = item.isSelected,
                        onClick = { onToggleSelection(item.list.id) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        item {
            TextButton(
                onClick = onCreateListClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(Res.string.btn_create_list))
            }
        }
    }

    Button(
        onClick = onConfirm,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingMedium, vertical = spacingCommon),
    ) {
        Text(stringResource(Res.string.btn_confirm))
    }
}

@Preview
@Composable
private fun PreviewAddToListBottomSheetLight() {
    AddToListBottomSheetPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewAddToListBottomSheetDark() {
    AddToListBottomSheetPreview(darkTheme = true)
}

@Composable
private fun AddToListBottomSheetPreview(darkTheme: Boolean) {
    val spell = SampleSpellRepository.getFirst()
    val spellRepository = SampleSpellRepository()
    val userListRepository = SampleUserListRepository()
    val bottomSheetProvider = SpellAddToListProvider(spellRepository, userListRepository)

    val body = AddToListState.Body.WithData(
        item = spell,
        selectableLists = SampleUserListRepository.getAll().map {
            AddToListState.SelectableUserList(it, alreadyAdded = it.itemIds.contains(spell.id))
        },
    )
    AppThemePreview(darkTheme = darkTheme) {
        Column {
            AddToListBottomSheetContent(
                body = body,
                header = bottomSheetProvider::Header,
                onToggleSelection = {},
                onConfirm = {},
                onCreateListClicked = {},
            )
        }
    }
}
