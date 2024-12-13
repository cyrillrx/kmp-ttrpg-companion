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
import com.cyrillrx.rpg.bestiary.presentation.component.BestiaryScreen
import com.cyrillrx.rpg.bestiary.presentation.viewmodel.BestiaryViewModel
import com.cyrillrx.rpg.bestiary.presentation.viewmodel.BestiaryViewModelFactory
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.magicalitems.presentation.component.InventoryScreen
import com.cyrillrx.rpg.magicalitems.presentation.viewmodel.InventoryViewModel
import com.cyrillrx.rpg.magicalitems.presentation.viewmodel.InventoryViewModelFactory
import com.cyrillrx.rpg.spellbook.presentation.component.AlternativeSpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.component.SpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.component.SpellCard
import com.cyrillrx.rpg.spellbook.presentation.viewmodel.SpellBookViewModel
import com.cyrillrx.rpg.spellbook.presentation.viewmodel.SpellBookViewModelFactory
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
