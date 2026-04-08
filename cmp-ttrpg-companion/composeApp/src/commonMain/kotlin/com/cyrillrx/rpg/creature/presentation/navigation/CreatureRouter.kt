package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation.NavController

interface CreatureRouter {
    fun navigateUp()
    fun openDetail(creatureId: String)
}

class CreatureRouterImpl(private val navController: NavController) : CreatureRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openDetail(creatureId: String) {
        navController.navigate(CreatureRoute.Detail(creatureId))
    }
}
