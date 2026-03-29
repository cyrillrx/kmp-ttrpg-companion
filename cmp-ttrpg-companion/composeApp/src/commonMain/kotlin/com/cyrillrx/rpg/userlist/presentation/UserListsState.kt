package com.cyrillrx.rpg.userlist.presentation

import com.cyrillrx.rpg.userlist.domain.UserList
import org.jetbrains.compose.resources.StringResource

data class UserListsState(
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body
        data object Empty : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val lists: List<UserList>) : Body
    }
}
