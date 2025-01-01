package com.cyrillrx.rpg.magicalitem.presentation

import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.resources.StringResource

data class MagicalItemListState(
    val searchQuery: String,
    val body: Body,
) {
    sealed interface Body {
        data object Loading : Body

        data object Empty : Body

        data class Error(val errorMessage: StringResource) : Body

        data class WithData(val searchResults: List<MagicalItem>) : Body
    }
}
