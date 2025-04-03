package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.spell.domain.Spell

interface SpellRouter {
    fun navigateUp()
    fun openSpellDetail(spell: Spell)
}

class SpellRouterImpl(private val navController: NavController) : SpellRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openSpellDetail(spell: Spell) {
        navController.navigate(SpellRoute.Detail(spell.serialize()))
    }
}
