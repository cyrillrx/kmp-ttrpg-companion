package com.cyrillrx.rpg.magicalitem.presentation

import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.resources.StringResource

data class MagicalItemListDetailState(
    val listName: String = "",
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body
        data object EmptyList : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val magicalItems: List<MagicalItem>) : Body
    }
}
