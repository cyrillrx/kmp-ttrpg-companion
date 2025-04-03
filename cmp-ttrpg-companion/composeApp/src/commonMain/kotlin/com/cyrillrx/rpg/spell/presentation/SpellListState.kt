package com.cyrillrx.rpg.spell.presentation

import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.resources.StringResource

data class SpellListState(
    val searchQuery: String,
    val body: Body,
) {
    sealed interface Body {
        data object Loading : Body

        data object Empty : Body

        data class Error(val errorMessage: StringResource) : Body

        data class WithData(val searchResults: List<Spell>) : Body
    }
}
