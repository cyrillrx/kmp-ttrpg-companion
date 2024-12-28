package com.cyrillrx.rpg.creature.presentation

import com.cyrillrx.rpg.creature.domain.Creature

sealed interface BestiaryAction {
    data class OnSearchQueryChanged(val query: String) : BestiaryAction
    data class OnItemClicked(val item: Creature) : BestiaryAction
    data class OnSaveSpellClicked(val item: Creature) : BestiaryAction
}
