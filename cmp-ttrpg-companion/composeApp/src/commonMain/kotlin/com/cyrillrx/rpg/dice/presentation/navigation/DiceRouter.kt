package com.cyrillrx.rpg.dice.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp

interface DiceRouter {
    fun navigateUp()
}

class DiceRouterImpl(private val backStack: NavBackStack<NavKey>) : DiceRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }
}
