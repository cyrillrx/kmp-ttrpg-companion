package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem

interface MagicalItemRouter {
    fun navigateUp()
    fun openCompendium()
    fun openDetail(magicalItem: MagicalItem)
}

class MagicalItemRouterImpl(private val backStack: NavBackStack<NavKey>) : MagicalItemRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCompendium() {
        backStack.add(MagicalItemRoute.Compendium)
    }

    override fun openDetail(magicalItem: MagicalItem) {
        backStack.add(MagicalItemRoute.Detail(magicalItem.id))
    }
}
