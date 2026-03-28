package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyrillrx.rpg.core.presentation.AddToListState
import com.cyrillrx.rpg.core.presentation.viewmodel.AddToListViewModel
import com.cyrillrx.rpg.core.presentation.viewmodel.AddToListViewModelFactory
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.btn_create_list
import rpg_companion.composeapp.generated.resources.hint_list_name

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToListBottomSheet(
    itemId: String,
    listType: UserList.Type,
    userListRepository: UserListRepository,
    onDismiss: () -> Unit,
) {
    val viewModel = viewModel<AddToListViewModel>(
        key = "$listType:$itemId",
        factory = AddToListViewModelFactory(itemId, listType, userListRepository),
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        AddToListContent(
            state = state,
            onAddToList = { listId -> viewModel.addToList(listId, onDismiss) },
            onCreateAndAdd = { name -> viewModel.createAndAdd(name, onDismiss) },
        )
    }
}

@Composable
private fun AddToListContent(
    state: AddToListState,
    onAddToList: (String) -> Unit,
    onCreateAndAdd: (String) -> Unit,
) {
    var showCreateField by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = spacingMedium),
    ) {
        Text(
            text = stringResource(Res.string.btn_add_to_list),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(spacingMedium),
        )

        when (val body = state.body) {
            is AddToListState.Body.Loading -> Loader()
            is AddToListState.Body.WithData -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = spacingMedium),
                    verticalArrangement = Arrangement.spacedBy(spacingSmall),
                ) {
                    items(body.lists, key = { it.id }) { list ->
                        Button(
                            onClick = { onAddToList(list.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        ) {
                            Text(list.name)
                        }
                    }
                }
            }
        }

        if (showCreateField) {
            TextField(
                value = newListName,
                onValueChange = { newListName = it },
                placeholder = { Text(stringResource(Res.string.hint_list_name)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium, vertical = spacingSmall),
            )
            Button(
                onClick = {
                    if (newListName.isNotBlank()) {
                        onCreateAndAdd(newListName)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium),
            ) {
                Text(stringResource(Res.string.btn_create_list))
            }
        } else {
            TextButton(
                onClick = { showCreateField = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium, vertical = spacingSmall),
            ) {
                Text(stringResource(Res.string.btn_create_list))
            }
        }
    }
}
