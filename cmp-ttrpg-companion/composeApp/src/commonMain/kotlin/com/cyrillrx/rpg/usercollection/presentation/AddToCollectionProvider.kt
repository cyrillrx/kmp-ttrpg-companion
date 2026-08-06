package com.cyrillrx.rpg.usercollection.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.presentation.component.AddToCollectionBottomSheet
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.AddToCollectionViewModel
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.AddToCollectionViewModelFactory

interface AddToCollectionProvider<T> {
    val listType: UserCollection.Type
    val viewModelFactory: AddToCollectionViewModelFactory<T>

    @Composable
    fun Header(entity: T)

    @Composable
    fun BottomSheet(
        entityId: String,
        onDismiss: () -> Unit,
    ) {
        val viewModel = viewModel<AddToCollectionViewModel<T>>(key = listType.name, factory = viewModelFactory)
        LaunchedEffect(entityId) { viewModel.loadEntity(entityId) }
        AddToCollectionBottomSheet(viewModel, ::Header, onDismiss = onDismiss)
    }
}
