package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

interface CreatureRouter {
    fun navigateUp()
    fun openList()
    fun openDetail(creatureId: String)
}

class CreatureRouterImpl(private val backStack: NavBackStack<NavKey>) : CreatureRouter {
    override fun navigateUp() {
        if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
    }

    override fun openList() {
        backStack.add(CreatureRoute.List)
    }

    override fun openDetail(creatureId: String) {
        backStack.add(CreatureRoute.Detail(creatureId))
    }
}
