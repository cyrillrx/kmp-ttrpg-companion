package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.rpg.spell.domain.Spell

interface SpellRouter {
    fun navigateUp()
    fun openSpellDetail(spell: Spell)
    fun openMySpellLists() {}
    fun openSpellListDetail(listId: String) {}
}

class SpellRouterImpl(private val navController: NavController) : SpellRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openSpellDetail(spell: Spell) {
        navController.navigate(SpellRoute.Detail(spell.id))
    }

    override fun openMySpellLists() {
        navController.navigate(SpellRoute.MyLists)
    }

    override fun openSpellListDetail(listId: String) {
        navController.navigate(SpellRoute.ListDetail(listId))
    }
}
