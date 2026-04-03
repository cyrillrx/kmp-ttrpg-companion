package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.navigation.NavController

interface MagicalItemRouter {
    fun navigateUp()
    fun openDetail(id: String)
    fun openAddToList(magicalItemId: String) {}
}

class MagicalItemRouterImpl(private val navController: NavController) : MagicalItemRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openDetail(id: String) {
        navController.navigate(MagicalItemRoute.Detail(id))
    }

    override fun openAddToList(magicalItemId: String) {
        navController.navigate(MagicalItemRoute.AddToList(magicalItemId))
    }
}
