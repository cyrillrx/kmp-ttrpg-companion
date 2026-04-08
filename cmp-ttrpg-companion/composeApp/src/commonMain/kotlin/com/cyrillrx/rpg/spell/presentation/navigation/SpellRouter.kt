package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.navigation.NavController

interface SpellRouter {
    fun navigateUp()
    fun openDetail(spellId: String)
}

class SpellRouterImpl(private val navController: NavController) : SpellRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openDetail(spellId: String) {
        navController.navigate(SpellRoute.Detail(spellId))
    }
}
