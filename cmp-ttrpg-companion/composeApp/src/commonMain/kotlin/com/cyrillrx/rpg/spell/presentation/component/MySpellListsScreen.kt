package com.cyrillrx.rpg.spell.presentation.component

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.ConfirmDeleteDialog
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.presentation.MySpellListsState
import com.cyrillrx.rpg.spell.presentation.viewmodel.MySpellListsViewModel
import com.cyrillrx.rpg.userlist.domain.UserList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_cancel
import rpg_companion.composeapp.generated.resources.btn_create_list
import rpg_companion.composeapp.generated.resources.btn_my_spell_lists
import rpg_companion.composeapp.generated.resources.dialog_delete_list_message
import rpg_companion.composeapp.generated.resources.hint_list_name
import rpg_companion.composeapp.generated.resources.no_result_found

@Composable
fun MySpellListsScreen(viewModel: MySpellListsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MySpellListsScreen(
        state = state,
        onNavigateUpClicked = viewModel::onNavigateUpClicked,
        onCreateList = viewModel::createList,
        onDeleteList = viewModel::deleteList,
        onOpenList = viewModel::openList,
    )
}

@Composable
fun MySpellListsScreen(
    state: MySpellListsState,
    onNavigateUpClicked: () -> Unit,
    onCreateList: (String) -> Unit,
    onDeleteList: (String) -> Unit,
    onOpenList: (UserList) -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<UserList?>(null) }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = stringResource(Res.string.btn_my_spell_lists),
                navigateUp = onNavigateUpClicked,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(Res.string.btn_create_list))
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val body = state.body) {
                is MySpellListsState.Body.Loading -> Loader()
                is MySpellListsState.Body.Empty -> {
                    Text(
                        text = stringResource(Res.string.no_result_found),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(spacingMedium),
                    )
                }
                is MySpellListsState.Body.WithData -> SpellListNames(
                    lists = body.lists,
                    onOpenList = onOpenList,
                    onDeleteList = { listToDelete = it },
                )
            }
        }
    }

    if (showCreateDialog) {
        CreateListDialog(
            onConfirm = { name ->
                onCreateList(name)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false },
        )
    }

    listToDelete?.let { list ->
        ConfirmDeleteDialog(
            message = stringResource(Res.string.dialog_delete_list_message, list.name),
            onConfirm = {
                onDeleteList(list.id)
                listToDelete = null
            },
            onDismiss = { listToDelete = null },
        )
    }
}

@Composable
private fun SpellListNames(
    lists: List<UserList>,
    onOpenList: (UserList) -> Unit,
    onDeleteList: (UserList) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(lists, key = { it.id }) { list ->
            SpellListNameItem(
                list = list,
                onClick = { onOpenList(list) },
                onDelete = { onDeleteList(list) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SpellListNameItem(
    list: UserList,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingSmall),
        ) {
            Text(
                text = list.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun CreateListDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.btn_create_list)) },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text(stringResource(Res.string.hint_list_name)) },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onConfirm(name) },
            ) {
                Text(stringResource(Res.string.btn_create_list))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

@Preview
@Composable
private fun PreviewMySpellListsScreenLight() {
    AppThemePreview(darkTheme = false) {
        MySpellListsScreen(
            state = MySpellListsState(
                body = MySpellListsState.Body.WithData(
                    lists = listOf(
                        UserList("1", "Sorts de combat", UserList.Type.SPELL, listOf("spell1")),
                        UserList("2", "Sorts de soutien", UserList.Type.SPELL, emptyList()),
                    ),
                ),
            ),
            onNavigateUpClicked = {},
            onCreateList = {},
            onDeleteList = {},
            onOpenList = {},
        )
    }
}

@Preview
@Composable
private fun PreviewMySpellListsScreenDark() {
    AppThemePreview(darkTheme = true) {
        MySpellListsScreen(
            state = MySpellListsState(body = MySpellListsState.Body.Empty),
            onNavigateUpClicked = {},
            onCreateList = {},
            onDeleteList = {},
            onOpenList = {},
        )
    }
}
