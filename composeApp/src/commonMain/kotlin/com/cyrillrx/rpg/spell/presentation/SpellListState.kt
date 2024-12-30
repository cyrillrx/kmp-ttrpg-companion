package com.cyrillrx.rpg.spell.presentation

import com.cyrillrx.rpg.spell.domain.Spell

data class SpellListState(
    val searchQuery: String = "",
    val body: Body,
) {
    sealed class Body {
        data object Loading : Body()

        data object Empty : Body()

        data class Error(val errorMessage: String) : Body()

        data class WithData(val searchResults: List<Spell>) : Body()
    }
}
