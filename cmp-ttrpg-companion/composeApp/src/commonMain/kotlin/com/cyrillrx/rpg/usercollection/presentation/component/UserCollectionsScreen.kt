package com.cyrillrx.rpg.usercollection.presentation.component

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.component.SwipeToDelete
import com.cyrillrx.rpg.core.presentation.component.dialog.CreateListDialog
import com.cyrillrx.rpg.core.presentation.component.rememberOptimisticDeleteHandler
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.usercollection.data.SampleUserCollectionRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.presentation.UserCollectionsState
import com.cyrillrx.rpg.usercollection.presentation.navigation.UserCollectionRouter
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.UserCollectionsViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_create_list
import rpg_companion.composeapp.generated.resources.no_result_found
import rpg_companion.composeapp.generated.resources.snackbar_error_deleting_list
import rpg_companion.composeapp.generated.resources.snackbar_list_deleted

@Composable
fun UserCollectionsScreen(viewModel: UserCollectionsViewModel, router: UserCollectionRouter, title: String) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.silentRefresh()
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.commitAllPendingDeletions() }
    }

    UserCollectionsScreen(
        state = state,
        title = title,
        events = viewModel.events,
        onNavigateUpClicked = router::navigateUp,
        onAddBtnClicked = viewModel::createList,
        onDeleteListOptimistically = viewModel::deleteListOptimistically,
        onUndoDeletion = viewModel::undoDeletion,
        onCommitDeletion = viewModel::commitDeletion,
        onCollectionClicked = router::openUserCollection,
    )
}

@Composable
fun UserCollectionsScreen(
    state: UserCollectionsState,
    title: String,
    events: SharedFlow<UserCollectionsViewModel.Event>,
    onNavigateUpClicked: () -> Unit,
    onAddBtnClicked: (String) -> Unit,
    onDeleteListOptimistically: (Stored<UserCollection>) -> UserCollectionsViewModel.PendingDeletion?,
    onUndoDeletion: (UserCollectionsViewModel.PendingDeletion) -> Unit,
    onCommitDeletion: (UserCollectionsViewModel.PendingDeletion) -> Unit,
    onCollectionClicked: (UserCollection) -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is UserCollectionsViewModel.Event.DeletionError -> {
                    val errorMessage = getString(Res.string.snackbar_error_deleting_list, event.list.name)
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }

    val onDeleteList = rememberOptimisticDeleteHandler(
        snackbarHostState = snackbarHostState,
        onDeleteOptimistically = onDeleteListOptimistically,
        onUndo = onUndoDeletion,
        onCommit = onCommitDeletion,
        getMessage = { stored -> getString(Res.string.snackbar_list_deleted, stored.value.name) },
    )

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
                is UserCollectionsState.Body.Loading -> Loader()
                is UserCollectionsState.Body.Empty -> ErrorLayout(Res.string.no_result_found)
                is UserCollectionsState.Body.Error -> ErrorLayout(body.errorMessage)
                is UserCollectionsState.Body.WithData -> UserCollections(
                    lists = body.lists,
                    onCollectionClicked = onCollectionClicked,
                    onDeleteList = onDeleteList,
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
private fun UserCollections(
    lists: List<Stored<UserCollection>>,
    onCollectionClicked: (UserCollection) -> Unit,
    onDeleteList: (Stored<UserCollection>) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
    ) {
        items(lists, key = { it.value.id }) { stored ->
            SwipeToDelete(
                onSwiped = { onDeleteList(stored) },
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
            ) {
                UserCollectionItem(
                    list = stored.value,
                    updatedAt = stored.updatedAt,
                    onClick = { onCollectionClicked(stored.value) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewUserCollectionsScreenLight() {
    UserCollectionsScreenPreview(false)
}

@Preview
@Composable
private fun PreviewUserCollectionsScreenDark() {
    UserCollectionsScreenPreview(true)
}

@Composable
private fun UserCollectionsScreenPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        UserCollectionsScreen(
            state = UserCollectionsState(
                body = UserCollectionsState.Body.WithData(
                    lists = SampleUserCollectionRepository.getAll(),
                ),
            ),
            title = "Spellbooks",
            events = MutableSharedFlow(),
            onNavigateUpClicked = {},
            onAddBtnClicked = {},
            onDeleteListOptimistically = { null },
            onUndoDeletion = {},
            onCommitDeletion = {},
            onCollectionClicked = {},
        )
    }
}
