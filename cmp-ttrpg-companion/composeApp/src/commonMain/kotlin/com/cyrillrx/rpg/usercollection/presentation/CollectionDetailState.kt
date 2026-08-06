package com.cyrillrx.rpg.usercollection.presentation

import org.jetbrains.compose.resources.StringResource

data class CollectionDetailState<T>(
    val collectionName: String = "",
    val body: Body<T> = Body.Loading,
) {
    sealed interface Body<out T> {
        data object Loading : Body<Nothing>
        data object Empty : Body<Nothing>
        data class Error(val errorMessage: StringResource) : Body<Nothing>
        data class WithData<T>(val items: List<T>) : Body<T>
    }
}
