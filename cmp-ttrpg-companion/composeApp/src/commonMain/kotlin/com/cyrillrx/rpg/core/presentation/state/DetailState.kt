package com.cyrillrx.rpg.core.presentation.state

sealed interface DetailState<out T> {
    data object Loading : DetailState<Nothing>
    data class Found<T>(val item: T) : DetailState<T>
    data class NotFound(val id: String) : DetailState<Nothing>

    companion object {
        fun <T> of(id: String, item: T?): DetailState<T> = if (item == null) NotFound(id) else Found(item)
    }
}
