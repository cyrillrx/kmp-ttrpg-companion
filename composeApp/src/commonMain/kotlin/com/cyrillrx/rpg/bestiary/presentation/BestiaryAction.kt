package com.cyrillrx.rpg.bestiary.presentation

import com.cyrillrx.rpg.bestiary.domain.Creature

sealed interface BestiaryAction {
    data class OnSearchQueryChanged(val query: String) : BestiaryAction
    data class OnItemClicked(val item: Creature) : BestiaryAction
    data class OnSaveSpellClicked(val item: Creature) : BestiaryAction
}
