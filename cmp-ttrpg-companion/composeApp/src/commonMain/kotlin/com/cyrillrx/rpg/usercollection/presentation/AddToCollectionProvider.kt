package com.cyrillrx.rpg.userlist.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.presentation.component.AddToListBottomSheet
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddToListViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddToListViewModelFactory

interface AddToListProvider<T> {
    val listType: UserList.Type
    val viewModelFactory: AddToListViewModelFactory<T>

    @Composable
    fun Header(entity: T)

    @Composable
    fun BottomSheet(
        entityId: String,
        onDismiss: () -> Unit,
    ) {
        val viewModel = viewModel<AddToListViewModel<T>>(key = listType.name, factory = viewModelFactory)
        LaunchedEffect(entityId) { viewModel.loadEntity(entityId) }
        AddToListBottomSheet(viewModel, ::Header, onDismiss = onDismiss)
    }
}
