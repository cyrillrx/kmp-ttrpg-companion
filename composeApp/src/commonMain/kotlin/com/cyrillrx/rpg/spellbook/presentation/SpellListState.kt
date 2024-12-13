package com.cyrillrx.rpg.spellbook.presentation

import com.cyrillrx.rpg.spellbook.domain.Spell

sealed class SpellListState(
    open val searchQuery: String = "",
    val savedSpells: List<Spell> = emptyList(),
) {
    data class Loading(override val searchQuery: String) : SpellListState()

    data class Empty(override val searchQuery: String) : SpellListState()

    data class Error(
        override val searchQuery: String,
        val errorMessage: String,
    ) : SpellListState()

    data class WithData(
        override val searchQuery: String,
        val searchResults: List<Spell> = emptyList(),
    ) : SpellListState()
}
