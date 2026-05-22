package com.cyrillrx.rpg.settings.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.settings.presentation.SettingsScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

sealed interface SettingsRoute {
    @Serializable
    data object Main : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerSettingsRoutes() {
    subclass(SettingsRoute.Main::class, SettingsRoute.Main.serializer())
}

fun EntryProviderScope<NavKey>.handleSettingsRoutes(
    backStack: NavBackStack<NavKey>,
) {
    entry<SettingsRoute.Main> {
        SettingsScreen(router = SettingsRouterImpl(backStack))
    }
}
