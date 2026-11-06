package com.cyrillrx.rpg.dice.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.presentation.component.DiceHistoryScreen
import com.cyrillrx.rpg.dice.presentation.component.DiceRollerScreen
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceHistoryViewModel
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceHistoryViewModelFactory
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceRollerViewModel
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceRollerViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

sealed interface DiceRoute {
    @Serializable
    data object Roller : NavKey

    @Serializable
    data object History : NavKey
}

private const val ROLLER_HISTORY_KEY = "roller-history"

fun PolymorphicModuleBuilder<NavKey>.registerDiceRoutes() {
    subclass(DiceRoute.Roller::class, DiceRoute.Roller.serializer())
    subclass(DiceRoute.History::class, DiceRoute.History.serializer())
}

fun EntryProviderScope<NavKey>.handleDiceRoutes(
    backStack: NavBackStack<NavKey>,
    historyRepository: RollHistoryRepository,
) {
    entry<DiceRoute.Roller> {
        val viewModel = viewModel<DiceRollerViewModel>(factory = DiceRollerViewModelFactory(historyRepository))
        val historyViewModel = viewModel<DiceHistoryViewModel>(
            key = ROLLER_HISTORY_KEY,
            factory = DiceHistoryViewModelFactory(historyRepository),
        )
        DiceRollerScreen(viewModel, historyViewModel, DiceRouterImpl(backStack))
    }
    entry<DiceRoute.History> {
        val viewModel = viewModel<DiceHistoryViewModel>(factory = DiceHistoryViewModelFactory(historyRepository))
        DiceHistoryScreen(viewModel, DiceRouterImpl(backStack))
    }
}
