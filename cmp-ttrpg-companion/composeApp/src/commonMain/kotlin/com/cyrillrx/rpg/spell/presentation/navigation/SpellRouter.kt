package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

interface SpellRouter {
    fun navigateUp()
    fun openDetail(spellId: String)
}

class SpellRouterImpl(private val backStack: NavBackStack<NavKey>) : SpellRouter {
    override fun navigateUp() {
        if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
    }

    override fun openDetail(spellId: String) {
        backStack.add(SpellRoute.Detail(spellId))
    }
}
