package com.cyrillrx.rpg.spellbook.presentation

import com.cyrillrx.rpg.spellbook.data.api.ApiSpell

data class SpellListState(
    val searchQuery: String = "",
    val searchResults: List<ApiSpell> = emptyList(),
    val savedSpells: List<ApiSpell> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
