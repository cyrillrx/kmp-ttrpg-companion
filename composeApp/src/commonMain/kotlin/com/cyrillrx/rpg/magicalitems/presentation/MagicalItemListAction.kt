package com.cyrillrx.rpg.magicalitems.presentation

import com.cyrillrx.rpg.magicalitems.domain.MagicalItem

sealed interface MagicalItemListAction {
    data class OnSearchQueryChanged(val query: String) : MagicalItemListAction
    data class OnItemClicked(val item: MagicalItem) : MagicalItemListAction
    data class OnSaveSpellClicked(val item: MagicalItem) : MagicalItemListAction
}
