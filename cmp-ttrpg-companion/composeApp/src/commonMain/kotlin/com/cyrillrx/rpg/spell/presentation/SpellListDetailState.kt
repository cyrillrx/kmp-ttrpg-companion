package com.cyrillrx.rpg.spell.presentation

import com.cyrillrx.rpg.spell.domain.Spell

data class SpellListDetailState(
    val listName: String = "",
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body
        data object Empty : Body
        data class WithData(val spells: List<Spell>) : Body
    }
}
