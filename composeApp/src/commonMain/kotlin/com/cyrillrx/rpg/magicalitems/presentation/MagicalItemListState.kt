package com.cyrillrx.rpg.magicalitems.presentation

import com.cyrillrx.rpg.magicalitems.domain.MagicalItem

sealed class MagicalItemListState(
    open val searchQuery: String = "",
) {
    data class Loading(override val searchQuery: String) : MagicalItemListState()

    data class Empty(override val searchQuery: String) : MagicalItemListState()

    data class Error(
        override val searchQuery: String,
        val errorMessage: String,
    ) : MagicalItemListState()

    data class WithData(
        override val searchQuery: String,
        val searchResults: List<MagicalItem> = emptyList(),
    ) : MagicalItemListState()
}
