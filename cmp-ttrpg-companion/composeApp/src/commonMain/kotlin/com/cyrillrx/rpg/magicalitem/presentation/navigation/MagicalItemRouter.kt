package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.navigation.NavController

interface MagicalItemRouter {
    fun navigateUp()
    fun openDetail(magicalItemId: String)
}

class MagicalItemRouterImpl(private val navController: NavController) : MagicalItemRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openDetail(magicalItemId: String) {
        navController.navigate(MagicalItemRoute.Detail(magicalItemId))
    }
}
