package com.cyrillrx.rpg.core.presentation

import com.cyrillrx.rpg.userlist.domain.UserList

data class AddToListState(
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body
        data class WithData(val lists: List<UserList>) : Body
    }
}
