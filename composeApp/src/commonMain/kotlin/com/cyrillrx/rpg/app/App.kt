package com.cyrillrx.rpg.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyrillrx.rpg.campaign.presentation.navigation.handleCampaignRoutes
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.creature.presentation.component.BestiaryScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.BestiaryViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.BestiaryViewModelFactory
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouterImpl
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
            val homeRouter = HomeRouterImpl(navController)
            composable<Route.Home> { HomeScreen(homeRouter) }

            handleCampaignRoutes(navController)

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

            composable<Route.CharacterSheetList> {
                val viewModelFactory = BestiaryViewModelFactory(fileReader)
                val viewModel = viewModel<BestiaryViewModel>(factory = viewModelFactory)
                BestiaryScreen(viewModel)
            }
        }
    }
}
