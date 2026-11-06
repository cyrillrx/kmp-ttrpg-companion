package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp
import com.cyrillrx.rpg.spell.domain.Spell

interface SpellRouter {
    fun navigateUp()
    fun openCompendium()
    fun openDetail(spell: Spell)
}

class SpellRouterImpl(private val backStack: NavBackStack<NavKey>) : SpellRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCompendium() {
        backStack.add(SpellRoute.Compendium)
    }

    override fun openDetail(spell: Spell) {
        backStack.add(SpellRoute.Detail(spell.id))
    }
}
