package com.cyrillrx.rpg.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.creature.presentation.component.BestiaryScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.BestiaryViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.BestiaryViewModelFactory
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.magicalitems.presentation.component.InventoryScreen
import com.cyrillrx.rpg.magicalitems.presentation.viewmodel.InventoryViewModel
import com.cyrillrx.rpg.magicalitems.presentation.viewmodel.InventoryViewModelFactory
import com.cyrillrx.rpg.spell.presentation.navigation.handleSpellRoutes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        val navController = rememberNavController()

        val fileReader = ComposeFileReader()

        NavHost(
            navController = navController,
            startDestination = Route.Home,
        ) {
            val homeRouter = createHomeRouter(navController)

            composable<Route.Home> { HomeScreen(homeRouter) }

            handleSpellRoutes(navController, fileReader)

            composable<Route.Bestiary> {
                val viewModelFactory = BestiaryViewModelFactory(fileReader)
                val viewModel = viewModel<BestiaryViewModel>(factory = viewModelFactory)
                BestiaryScreen(viewModel)
            }

            composable<Route.MagicalItems> {
                val viewModelFactory = InventoryViewModelFactory(fileReader)
                val viewModel = viewModel<InventoryViewModel>(factory = viewModelFactory)
                InventoryScreen(viewModel)
            }
        }
    }
}

private fun createHomeRouter(navController: NavController): HomeRouter {
    return object : HomeRouter {
        override fun openSpellBook() {
            navController.navigate(SpellRoute.List)
        }

        override fun openAlternativeSpellBook() {
            navController.navigate(SpellRoute.AlternativeList)
        }

        override fun openMagicalItems() {
            navController.navigate(Route.MagicalItems)
        }

        override fun openBestiary() {
            navController.navigate(Route.Bestiary)
        }
    }
}
