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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.component.dialog.ConfirmDeleteDialog
import com.cyrillrx.rpg.core.presentation.component.dialog.CreateListDialog
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.presentation.UserListsState
import com.cyrillrx.rpg.userlist.presentation.viewmodel.UserListsViewModel
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
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(Res.string.btn_create_list),
                )
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
                is UserListsState.Body.Empty -> ErrorLayout(Res.string.no_result_found)
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
            SwipeableUserListItem(
                list = list,
                onClick = { onListClicked(list) },
                onDelete = { onDeleteBtnClicked(list) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SwipeableUserListItem(
    list: UserList,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
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
        UserListItem(
            list = list,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        )
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
