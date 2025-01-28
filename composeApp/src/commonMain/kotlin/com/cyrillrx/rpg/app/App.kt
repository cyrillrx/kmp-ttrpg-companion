package com.cyrillrx.rpg.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyrillrx.rpg.campaign.navigation.handleCampaignRoutes
import com.cyrillrx.rpg.character.presentation.navigation.handlePlayerCharacterRoutes
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.creature.data.JsonCreatureRepository
import com.cyrillrx.rpg.creature.presentation.navigation.handleCreatureRoutes
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouterImpl
import com.cyrillrx.rpg.magicalitem.presentation.navigation.handleMagicalItemRoutes
import com.cyrillrx.rpg.spell.presentation.navigation.handleSpellRoutes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(dbDriverFactory: DatabaseDriverFactory) {
    AppTheme {
        val navController = rememberNavController()

        val fileReader = ComposeFileReader()

        NavHost(
            navController = navController,
            startDestination = MainRoute.Home,
        ) {
            val homeRouter = HomeRouterImpl(navController)
            composable<MainRoute.Home> { HomeScreen(homeRouter) }

            handleCampaignRoutes(navController, dbDriverFactory)
            handlePlayerCharacterRoutes(navController)

            handleSpellRoutes(navController, fileReader)
            handleMagicalItemRoutes(navController, fileReader)
            handleCreatureRoutes(navController, JsonCreatureRepository(fileReader))
        }
    }
}
