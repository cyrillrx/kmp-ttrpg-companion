package com.cyrillrx.rpg.dice.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.dice.presentation.component.DiceRollerScreen
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceRollerViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

sealed interface DiceRoute {
    @Serializable
    data object Roller : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerDiceRoutes() {
    subclass(DiceRoute.Roller::class, DiceRoute.Roller.serializer())
}

fun EntryProviderScope<NavKey>.handleDiceRoutes(backStack: NavBackStack<NavKey>) {
    entry<DiceRoute.Roller> {
        val viewModel = viewModel { DiceRollerViewModel() }
        DiceRollerScreen(viewModel, DiceRouterImpl(backStack))
    }
}
