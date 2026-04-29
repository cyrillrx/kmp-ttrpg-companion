package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp

interface MonsterRouter {
    fun navigateUp()
    fun openCompendium()
    fun openDetail(monsterId: String)
}

class MonsterRouterImpl(private val backStack: NavBackStack<NavKey>) : MonsterRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCompendium() {
        backStack.add(MonsterRoute.Compendium)
    }

    override fun openDetail(monsterId: String) {
        backStack.add(MonsterRoute.Detail(monsterId))
    }
}
