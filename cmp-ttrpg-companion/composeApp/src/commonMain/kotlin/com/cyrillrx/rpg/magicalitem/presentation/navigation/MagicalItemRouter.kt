package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp

interface MagicalItemRouter {
    fun navigateUp()
    fun openList()
    fun openDetail(magicalItemId: String)
}

class MagicalItemRouterImpl(private val backStack: NavBackStack<NavKey>) : MagicalItemRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openList() {
        backStack.add(MagicalItemRoute.List)
    }

    override fun openDetail(magicalItemId: String) {
        backStack.add(MagicalItemRoute.Detail(magicalItemId))
    }
}
