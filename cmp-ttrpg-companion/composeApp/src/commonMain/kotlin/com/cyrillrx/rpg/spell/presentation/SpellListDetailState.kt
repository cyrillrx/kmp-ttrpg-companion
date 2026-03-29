package com.cyrillrx.rpg.spell.presentation

import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.resources.StringResource

data class SpellListDetailState(
    val listName: String = "",
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body
        data object EmptyList : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val spells: List<Spell>) : Body
    }
}
