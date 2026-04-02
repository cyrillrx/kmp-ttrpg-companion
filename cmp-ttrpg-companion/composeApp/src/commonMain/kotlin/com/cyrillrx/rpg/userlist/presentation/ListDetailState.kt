package com.cyrillrx.rpg.userlist.presentation

import org.jetbrains.compose.resources.StringResource

data class ListDetailState<T>(
    val listName: String = "",
    val body: Body<T> = Body.Loading,
) {
    sealed interface Body<out T> {
        data object Loading : Body<Nothing>
        data object EmptyList : Body<Nothing>
        data class Error(val errorMessage: StringResource) : Body<Nothing>
        data class WithData<T>(val items: List<T>) : Body<T>
    }
}
