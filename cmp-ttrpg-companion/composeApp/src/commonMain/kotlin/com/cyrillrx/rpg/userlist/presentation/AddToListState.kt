package com.cyrillrx.rpg.userlist.presentation

import com.cyrillrx.rpg.userlist.domain.UserList
import org.jetbrains.compose.resources.StringResource

data class AddToListState<T>(
    val body: Body<T> = Body.Loading,
) {
    data class SelectableUserList(val list: UserList, val alreadyAdded: Boolean, val isSelected: Boolean = alreadyAdded)

    sealed interface Body<out T> {
        data object Loading : Body<Nothing>
        data class Error(val errorMessage: StringResource) : Body<Nothing>
        data class WithData<T>(
            val item: T,
            val selectableLists: List<SelectableUserList>,
        ) : Body<T>
    }
}
