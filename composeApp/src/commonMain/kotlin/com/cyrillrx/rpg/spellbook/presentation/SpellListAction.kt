package com.cyrillrx.rpg.spellbook.presentation

import com.cyrillrx.rpg.spellbook.data.api.ApiSpell

sealed interface SpellListAction {
    data class OnSearchQueryChanged(val query: String) : SpellListAction
    data class OnSpellClicked(val spell: ApiSpell) : SpellListAction
    data class OnSaveSpellClicked(val spell: ApiSpell) : SpellListAction
}
