package com.cyrillrx.rpg.spellbook.presentation

import com.cyrillrx.rpg.spellbook.domain.Spell

data class SpellListState(
    val searchQuery: String = "",
    val searchResults: List<Spell> = emptyList(),
    val savedSpells: List<Spell> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
