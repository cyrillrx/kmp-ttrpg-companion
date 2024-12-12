package com.cyrillrx.rpg.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.bestiary.presentation.BestiaryScreen
import com.cyrillrx.rpg.bestiary.presentation.BestiaryViewModel
import com.cyrillrx.rpg.bestiary.presentation.BestiaryViewModelFactory
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.magicalitems.presentation.InventoryScreen
import com.cyrillrx.rpg.magicalitems.presentation.InventoryViewModel
import com.cyrillrx.rpg.magicalitems.presentation.InventoryViewModelFactory
import com.cyrillrx.rpg.spellbook.presentation.AlternativeSpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.SpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.SpellBookViewModel
import com.cyrillrx.rpg.spellbook.presentation.SpellBookViewModelFactory
import com.cyrillrx.rpg.spellbook.presentation.SpellCard
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

            composable<Route.SpellList>(
                enterTransition = { slideInHorizontally { initialOffset -> initialOffset } },
                exitTransition = { slideOutHorizontally { initialOffset -> initialOffset } },
            ) {
                val viewModelFactory = SpellBookViewModelFactory(fileReader)
                val viewModel = viewModel<SpellBookViewModel>(factory = viewModelFactory)
                SpellBookScreen(viewModel) { spell -> navController.navigate(Route.SpellDetail(spell.serialize())) }
            }

            composable<Route.AlternativeSpellList> {
                val viewModelFactory = SpellBookViewModelFactory(fileReader)
                val viewModel = viewModel<SpellBookViewModel>(factory = viewModelFactory)
                AlternativeSpellBookScreen(viewModel)
            }

            composable<Route.SpellDetail> { entry ->
                val args = entry.toRoute<Route.SpellDetail>()
                SpellCard(args.serializedSpell.deserialize())
            }

            composable<Route.Bestiary> {
                val viewModelFactory = BestiaryViewModelFactory(fileReader)
                val viewModel = viewModel<BestiaryViewModel>(factory = viewModelFactory)
                BestiaryScreen(viewModel.creatures)
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
            navController.navigate(Route.SpellList)
        }

        override fun openAlternativeSpellBook() {
            navController.navigate(Route.AlternativeSpellList)
        }

        override fun openMagicalItems() {
            navController.navigate(Route.MagicalItems)
        }

        override fun openBestiary() {
            navController.navigate(Route.Bestiary)
        }
    }
}
