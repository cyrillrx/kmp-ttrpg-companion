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
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.userlist.presentation.AddToListState
import com.cyrillrx.rpg.userlist.presentation.EntityUiProvider
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddToListViewModel
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.btn_create_list

@Composable
fun <T> AddToListScreen(
    viewModel: AddToListViewModel<T>,
    uiProvider: EntityUiProvider<T>,
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.events.collect {
            when (it) {
                AddToListViewModel.Event.Dismiss -> onNavigateUp()
            }
        }
    }

    AddToListScreenContent(
        body = state.body,
        uiProvider = uiProvider,
        onNavigateUp = onNavigateUp,
        onToggleSelection = viewModel::toggleSelection,
        onConfirm = viewModel::confirmSelection,
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
private fun <T> AddToListScreenContent(
    body: AddToListState.Body<T>,
    uiProvider: EntityUiProvider<T>,
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
                is AddToListState.Body.Loading -> item { Loader() }
                is AddToListState.Body.Error -> item { ErrorLayout(body.errorMessage) }
                is AddToListState.Body.WithData -> {
                    item { uiProvider.Header(body.item) }
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
