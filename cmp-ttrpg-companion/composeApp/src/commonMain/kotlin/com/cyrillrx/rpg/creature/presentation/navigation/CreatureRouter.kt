package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp

interface CreatureRouter {
    fun navigateUp()
    fun openCompendium()
    fun openDetail(creatureId: String)
}

class CreatureRouterImpl(private val backStack: NavBackStack<NavKey>) : CreatureRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCompendium() {
        backStack.add(CreatureRoute.Compendium)
    }

    override fun openDetail(creatureId: String) {
        backStack.add(CreatureRoute.Detail(creatureId))
    }
}
