package com.cyrillrx.rpg.userlist.presentation

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
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.presentation.viewmodel.UserListsViewModel
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_create_list
import rpg_companion.composeapp.generated.resources.dialog_delete_list_message
import rpg_companion.composeapp.generated.resources.no_result_found

@Composable
fun UserListsScreen(viewModel: UserListsViewModel, title: String) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    UserListsScreen(
        state = state,
        title = title,
        onNavigateUpClicked = viewModel::onNavigateUpClicked,
        onAddBtnClicked = viewModel::createList,
        onDeleteBtnClicked = viewModel::deleteList,
        onListClicked = viewModel::openList,
    )
}

@Composable
fun UserListsScreen(
    state: UserListsState,
    title: String,
    onNavigateUpClicked: () -> Unit,
    onAddBtnClicked: (String) -> Unit,
    onDeleteBtnClicked: (String) -> Unit,
    onListClicked: (UserList) -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<UserList?>(null) }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = title,
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
                is UserListsState.Body.Loading -> Loader()
                is UserListsState.Body.Empty -> {
                    Text(
                        text = stringResource(Res.string.no_result_found),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(spacingMedium),
                    )
                }

                is UserListsState.Body.Error -> ErrorLayout(body.errorMessage)
                is UserListsState.Body.WithData -> UserLists(
                    lists = body.lists,
                    onListClicked = onListClicked,
                    onDeleteBtnClicked = { listToDelete = it },
                )
            }
        }
    }

    if (showCreateDialog) {
        CreateListDialog(
            onConfirm = { name ->
                onAddBtnClicked(name)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false },
        )
    }

    listToDelete?.let { list ->
        ConfirmDeleteDialog(
            message = stringResource(Res.string.dialog_delete_list_message, list.name),
            onConfirm = {
                onDeleteBtnClicked(list.id)
                listToDelete = null
            },
            onDismiss = { listToDelete = null },
        )
    }
}

@Composable
private fun UserLists(
    lists: List<UserList>,
    onListClicked: (UserList) -> Unit,
    onDeleteBtnClicked: (UserList) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(lists, key = { it.id }) { list ->
            UserListItem(
                list = list,
                onClick = { onListClicked(list) },
                onDelete = { onDeleteBtnClicked(list) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewUserListsScreenLight() {
    UserListsScreenPreview(false)
}

@Preview
@Composable
private fun PreviewUserListsScreenDark() {
    UserListsScreenPreview(true)
}

@Composable
private fun UserListsScreenPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        UserListsScreen(
            state = UserListsState(
                body = UserListsState.Body.WithData(
                    lists = SampleUserListRepository.getAll(),
                ),
            ),
            title = "Spellbooks",
            onNavigateUpClicked = {},
            onAddBtnClicked = {},
            onDeleteBtnClicked = {},
            onListClicked = {},
        )
    }
}
