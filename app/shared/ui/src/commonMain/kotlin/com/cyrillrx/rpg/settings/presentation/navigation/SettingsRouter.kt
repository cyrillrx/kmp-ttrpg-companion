package com.cyrillrx.rpg.settings.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp

interface SettingsRouter {
    fun navigateUp()
}

class SettingsRouterImpl(private val backStack: NavBackStack<NavKey>) : SettingsRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }
}
