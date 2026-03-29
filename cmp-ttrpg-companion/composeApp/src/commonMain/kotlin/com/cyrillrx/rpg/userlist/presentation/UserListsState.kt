package com.cyrillrx.rpg.userlist.presentation

import com.cyrillrx.rpg.userlist.domain.UserList

data class UserListsState(
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body
        data object Empty : Body
        data class WithData(val lists: List<UserList>) : Body
    }
}
