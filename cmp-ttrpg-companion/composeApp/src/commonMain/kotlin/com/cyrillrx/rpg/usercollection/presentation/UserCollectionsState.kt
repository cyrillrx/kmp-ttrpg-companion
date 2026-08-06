package com.cyrillrx.rpg.usercollection.presentation

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import org.jetbrains.compose.resources.StringResource

data class UserCollectionsState(
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body
        data object Empty : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val lists: List<Stored<UserCollection>>) : Body
    }
}
