package com.cyrillrx.rpg.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.app.MainRoute
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRoute
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigateUpTest {

    @Test
    fun `navigating up pops the top entry`() {
        val backStack = NavBackStack<NavKey>(MainRoute.Home, SpellRoute.Compendium)

        backStack.navigateUp()

        assertEquals(listOf<NavKey>(MainRoute.Home), backStack.toList())
    }

    @Test
    fun `navigating up twice from a single pushed entry keeps the root`() {
        val backStack = NavBackStack<NavKey>(MainRoute.Home, SpellRoute.Compendium)

        backStack.navigateUp()
        backStack.navigateUp()

        assertEquals(listOf<NavKey>(MainRoute.Home), backStack.toList())
    }
}
