package com.cyrillrx.rpg.usercollection.presentation

import com.cyrillrx.rpg.usercollection.domain.CollectionItemOrder
import org.jetbrains.compose.resources.StringResource

data class CollectionDetailState<T>(
    val collectionName: String = "",
    val body: Body<T> = Body.Loading,
    val sortOrder: CollectionItemOrder = CollectionItemOrder.ADDED,
) {
    /** Whether the collection itself is known, and so whether [collectionName] is meaningful. */
    val isLoaded: Boolean get() = body is Body.WithData || body is Body.Empty

    sealed interface Body<out T> {
        data object Loading : Body<Nothing>
        data object Empty : Body<Nothing>
        data class Error(val errorMessage: StringResource) : Body<Nothing>
        data class WithData<T>(val items: List<T>) : Body<T>
    }
}
