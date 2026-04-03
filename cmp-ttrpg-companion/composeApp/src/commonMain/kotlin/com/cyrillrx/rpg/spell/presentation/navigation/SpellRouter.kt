package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.rpg.userlist.presentation.navigation.ListDetailRouter

interface SpellRouter {
    fun navigateUp()
    fun openDetail(id: String)
    fun openMySpellLists() {}
    fun openSpellListDetail(listId: String) {}
    fun openAddToList(spellId: String) {}
}

class SpellRouterImpl(private val navController: NavController) : SpellRouter, ListDetailRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openDetail(id: String) {
        navController.navigate(SpellRoute.Detail(id))
    }

    override fun openMySpellLists() {
        navController.navigate(SpellRoute.UserLists)
    }

    override fun openSpellListDetail(listId: String) {
        navController.navigate(SpellRoute.UserListDetail(listId))
    }

    override fun openAddToList(spellId: String) {
        navController.navigate(SpellRoute.AddToList(spellId))
    }
}
