package com.cyrillrx.rpg.usercollection.presentation

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import org.jetbrains.compose.resources.StringResource

data class AddToCollectionState<T>(
    val body: Body<T> = Body.Loading,
) {
    data class SelectableUserCollection(
        val stored: Stored<UserCollection>,
        val alreadyAdded: Boolean,
        val isSelected: Boolean = alreadyAdded,
    ) {
        val collection: UserCollection = stored.value
    }

    sealed interface Body<out T> {
        data object Loading : Body<Nothing>
        data class Error(val errorMessage: StringResource) : Body<Nothing>
        data class WithData<T>(
            val item: T,
            val selectableCollections: List<SelectableUserCollection>,
        ) : Body<T>
    }
}
