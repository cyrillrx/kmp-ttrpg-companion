package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation.NavController

interface CreatureRouter {
    fun navigateUp()
    fun openDetail(id: String)
    fun openAddToList(creatureId: String) {}
}

class CreatureRouterImpl(private val navController: NavController) : CreatureRouter {
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
