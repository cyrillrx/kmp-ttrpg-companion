package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp
import com.cyrillrx.rpg.creature.domain.Monster

interface MonsterRouter {
    fun navigateUp()
    fun openCompendium()
    fun openDetail(monster: Monster)
}

class MonsterRouterImpl(private val backStack: NavBackStack<NavKey>) : MonsterRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCompendium() {
        backStack.add(MonsterRoute.Compendium)
    }

    override fun openDetail(monster: Monster) {
        backStack.add(MonsterRoute.Detail(monster.id))
    }
}
