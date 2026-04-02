package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.component.dialog.CreateListDialog
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddSpellToListState
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddSpellToListViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.btn_create_list

@Composable
fun AddSpellToListScreen(
    viewModel: AddSpellToListViewModel,
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.events.collect {
            when (it) {
                AddSpellToListViewModel.Event.Dismiss -> onNavigateUp()
            }
        }
    }

    AddToListScreenContent(
        body = state.body,
        onNavigateUp = onNavigateUp,
        onToggleSelection = { viewModel.toggleSelection(it) },
        onConfirm = { viewModel.confirmSelection() },
        onCreateListClicked = { showCreateDialog = true },
    )

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
private fun AddToListScreenContent(
    body: AddSpellToListState.Body,
    onNavigateUp: () -> Unit,
    onToggleSelection: (String) -> Unit,
    onConfirm: () -> Unit,
    onCreateListClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = stringResource(Res.string.btn_add_to_list),
                navigateUp = onNavigateUp,
            )
        },
        bottomBar = {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium, vertical = spacingCommon),
            ) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = spacingMedium,
                end = spacingMedium,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + spacingMedium,
            ),
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
        ) {
            when (body) {
                is AddSpellToListState.Body.Loading -> item { Loader() }
                is AddSpellToListState.Body.Error -> item { ErrorLayout(body.errorMessage) }
                is AddSpellToListState.Body.WithData -> {
                    item { AddSpellHeader(body.spell) }
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
    }
}

@Preview
@Composable
fun PreviewAddToListScreenLight() {
    PreviewAddToListScreen(darkTheme = false)
}

@Preview
@Composable
fun PreviewAddToListScreenDark() {
    PreviewAddToListScreen(darkTheme = true)
}

@Composable
private fun PreviewAddToListScreen(darkTheme: Boolean) {
    val spell = SampleSpellRepository.fireball()
    val lists = SampleUserListRepository.getAll()
    val body = AddSpellToListState.Body.WithData(
        spell = spell,
        selectableLists = lists.map { AddSpellToListState.SelectableUserList(it, alreadyAdded = false) },
    )
    AppThemePreview(darkTheme = darkTheme) {
        AddToListScreenContent(
            body = body,
            onNavigateUp = {},
            onToggleSelection = {},
            onConfirm = {},
            onCreateListClicked = {},
        )
    }
}
