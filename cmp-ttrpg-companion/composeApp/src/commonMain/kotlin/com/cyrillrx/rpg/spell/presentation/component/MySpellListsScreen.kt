package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.ConfirmDeleteDialog
import com.cyrillrx.rpg.core.presentation.component.CreateListDialog
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.presentation.MySpellListsState
import com.cyrillrx.rpg.spell.presentation.viewmodel.MySpellListsViewModel
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.presentation.UserListItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_create_list
import rpg_companion.composeapp.generated.resources.btn_my_spell_lists
import rpg_companion.composeapp.generated.resources.dialog_delete_list_message
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
            UserListItem(
                list = list,
                onClick = { onOpenList(list) },
                onDelete = { onDeleteList(list) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
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
