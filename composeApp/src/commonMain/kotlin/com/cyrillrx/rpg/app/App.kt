package com.cyrillrx.rpg.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cyrillrx.rpg.common.theme.AppTheme
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.spellbook.data.JsonSpellRepository
import com.cyrillrx.rpg.spellbook.presentation.AlternativeSpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.SpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.SpellBookViewModel
import com.cyrillrx.rpg.spellbook.presentation.SpellCard
import com.cyrillrx.utils.deserialize
import com.cyrillrx.utils.serialize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        val navController = rememberNavController()

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
                val viewModel = SpellBookViewModel(JsonSpellRepository())
                SpellBookScreen(viewModel) { spell -> navController.navigate(Route.SpellDetail(spell.serialize())) }
            }

            composable<Route.AlternativeSpellList> {
                val viewModel = SpellBookViewModel(JsonSpellRepository())
                AlternativeSpellBookScreen(viewModel)
            }

            composable<Route.SpellDetail> { entry ->
                val args = entry.toRoute<Route.SpellDetail>()
                SpellCard(args.serializedSpell.deserialize())
            }

            composable<Route.Bestiary> {
                // TODO
            }

            composable<Route.BestiaryDetail> {
                // TODO
            }

            composable<Route.MagicalItems> {
                // TODO
            }

            composable<Route.MagicalItemDetail> {
                // TODO
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

        override fun openBestiary() {
            navController.navigate(Route.Bestiary)
        }

        override fun openMagicalItems() {
            navController.navigate(Route.MagicalItems)
        }

        override fun openLegacyBestiary() {}
        override fun openLegacyInventory() {}
    }
}
