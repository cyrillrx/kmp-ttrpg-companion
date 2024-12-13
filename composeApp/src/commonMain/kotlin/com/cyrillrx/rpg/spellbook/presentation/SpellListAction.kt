package com.cyrillrx.rpg.spellbook.presentation

import com.cyrillrx.rpg.spellbook.domain.Spell

sealed interface SpellListAction {
    data class OnSearchQueryChanged(val query: String) : SpellListAction
    data class OnSpellClicked(val spell: Spell) : SpellListAction
    data class OnSaveSpellClicked(val spell: Spell) : SpellListAction
}
