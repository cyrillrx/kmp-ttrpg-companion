package com.cyrillrx.rpg.userlist.presentation.component

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.component.SwipeToDelete
import com.cyrillrx.rpg.core.presentation.component.dialog.CreateListDialog
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.presentation.UserListsState
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouter
import com.cyrillrx.rpg.userlist.presentation.viewmodel.UserListsViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_create_list
import rpg_companion.composeapp.generated.resources.btn_undo
import rpg_companion.composeapp.generated.resources.no_result_found
import rpg_companion.composeapp.generated.resources.snackbar_error_deleting_list
import rpg_companion.composeapp.generated.resources.snackbar_list_deleted

@Composable
fun UserListsScreen(viewModel: UserListsViewModel, router: UserListRouter, title: String) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.silentRefresh()
    }

    UserListsScreen(
        state = state,
        title = title,
        events = viewModel.events,
        onNavigateUpClicked = router::navigateUp,
        onAddBtnClicked = viewModel::createList,
        onDeleteListOptimistically = viewModel::deleteListOptimistically,
        onUndoDeletion = viewModel::undoDeletion,
        onCommitDeletion = viewModel::commitDeletion,
        onListClicked = viewModel::openList,
    )
}

@Composable
fun UserListsScreen(
    state: UserListsState,
    title: String,
    events: SharedFlow<UserListsViewModel.Event>,
    onNavigateUpClicked: () -> Unit,
    onAddBtnClicked: (String) -> Unit,
    onDeleteListOptimistically: (UserList) -> UserListsViewModel.PendingDeletion?,
    onUndoDeletion: (UserListsViewModel.PendingDeletion) -> Unit,
    onCommitDeletion: (UserListsViewModel.PendingDeletion) -> Unit,
    onListClicked: (UserList) -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val undoLabel = stringResource(Res.string.btn_undo)

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is UserListsViewModel.Event.DeletionError -> {
                    val errorMessage = getString(Res.string.snackbar_error_deleting_list, event.list.name)
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }

    fun onDeleteList(list: UserList) {
        val pending = onDeleteListOptimistically(list) ?: return

        coroutineScope.launch {
            val deletedMessage = getString(Res.string.snackbar_list_deleted, list.name)
            val result = snackbarHostState.showSnackbar(
                message = deletedMessage,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> onUndoDeletion(pending)
                SnackbarResult.Dismissed -> onCommitDeletion(pending)
            }
        }
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = title,
                onNavigateUpClicked = onNavigateUpClicked,
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    onDeleteList = ::onDeleteList,
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
}

@Composable
private fun UserLists(
    lists: List<UserList>,
    onListClicked: (UserList) -> Unit,
    onDeleteList: (UserList) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(lists, key = { it.id }) { list ->
            SwipeToDelete(
                onSwiped = { onDeleteList(list) },
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
            ) {
                UserListItem(
                    list = list,
                    onClick = { onListClicked(list) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
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
            events = MutableSharedFlow(),
            onNavigateUpClicked = {},
            onAddBtnClicked = {},
            onDeleteListOptimistically = { null },
            onUndoDeletion = {},
            onCommitDeletion = {},
            onListClicked = {},
        )
    }
}
