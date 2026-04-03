package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.rpg.userlist.presentation.navigation.ListDetailRouter

interface MagicalItemRouter {
    fun navigateUp()
    fun openDetail(id: String)
    fun openAddToList(magicalItemId: String) {}
}

class MagicalItemRouterImpl(private val navController: NavController) : MagicalItemRouter, ListDetailRouter {
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
