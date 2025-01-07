package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem

interface MagicalItemRouter {
    fun navigateUp()
    fun openMagicalItemDetail(magicalItem: MagicalItem)
}

class MagicalItemRouterImpl(private val navController: NavController) : MagicalItemRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openMagicalItemDetail(magicalItem: MagicalItem) {
        navController.navigate(MagicalItemRoute.Detail(magicalItem.serialize()))
    }
}
