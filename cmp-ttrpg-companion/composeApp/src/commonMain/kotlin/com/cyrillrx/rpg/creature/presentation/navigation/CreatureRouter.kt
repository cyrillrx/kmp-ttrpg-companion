package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.rpg.userlist.presentation.navigation.ListDetailRouter

interface CreatureRouter {
    fun navigateUp()
    fun openDetail(id: String)
    fun openAddToList(creatureId: String) {}
}

class CreatureRouterImpl(private val navController: NavController) : CreatureRouter, ListDetailRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openDetail(id: String) {
        navController.navigate(CreatureRoute.Detail(id))
    }

    override fun openAddToList(creatureId: String) {
        navController.navigate(CreatureRoute.AddToList(creatureId))
    }
}
