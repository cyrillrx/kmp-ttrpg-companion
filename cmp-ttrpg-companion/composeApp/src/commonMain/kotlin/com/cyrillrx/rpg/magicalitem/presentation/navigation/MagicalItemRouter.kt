package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

interface MagicalItemRouter {
    fun navigateUp()
    fun openDetail(magicalItemId: String)
}

class MagicalItemRouterImpl(private val backStack: NavBackStack<NavKey>) : MagicalItemRouter {
    override fun navigateUp() {
        if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
    }

    override fun openDetail(magicalItemId: String) {
        backStack.add(MagicalItemRoute.Detail(magicalItemId))
    }
}
