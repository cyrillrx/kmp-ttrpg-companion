package com.cyrillrx.rpg.app

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.cyrillrx.rpg.campaign.data.SQLDelightCampaignRepository
import com.cyrillrx.rpg.campaign.navigation.handleCampaignRoutes
import com.cyrillrx.rpg.campaign.navigation.registerCampaignRoutes
import com.cyrillrx.rpg.character.data.RamPlayerCharacterRepository
import com.cyrillrx.rpg.character.presentation.navigation.handlePlayerCharacterRoutes
import com.cyrillrx.rpg.character.presentation.navigation.registerCharacterRoutes
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.navigation.navigateUp
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.creature.data.JsonCreatureRepository
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRouterImpl
import com.cyrillrx.rpg.creature.presentation.navigation.handleCreatureRoutes
import com.cyrillrx.rpg.creature.presentation.navigation.registerCreatureRoutes
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouterImpl
import com.cyrillrx.rpg.magicalitem.data.JsonMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRouterImpl
import com.cyrillrx.rpg.magicalitem.presentation.navigation.handleMagicalItemRoutes
import com.cyrillrx.rpg.magicalitem.presentation.navigation.registerMagicalItemRoutes
import com.cyrillrx.rpg.spell.data.JsonSpellRepository
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouterImpl
import com.cyrillrx.rpg.spell.presentation.navigation.handleSpellRoutes
import com.cyrillrx.rpg.spell.presentation.navigation.registerSpellRoutes
import com.cyrillrx.rpg.userlist.data.SQLDelightUserListRepository
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouterImpl
import com.cyrillrx.rpg.userlist.presentation.navigation.handleUserListRoutes
import com.cyrillrx.rpg.userlist.presentation.navigation.registerUserListRoutes
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.ui.tooling.preview.Preview

private val navSavedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(MainRoute.Home::class, MainRoute.Home.serializer())

            registerCampaignRoutes()
            registerCharacterRoutes()

            registerSpellRoutes()
            registerMagicalItemRoutes()
            registerCreatureRoutes()

            registerUserListRoutes()
        }
    }
}

@Composable
@Preview
fun App(dbDriverFactory: DatabaseDriverFactory) {
    AppTheme {
        val backStack = rememberNavBackStack(navSavedStateConfig, MainRoute.Home)

        val fileReader = ComposeFileReader()
        val userListRepository = SQLDelightUserListRepository(dbDriverFactory)

        NavDisplay(
            backStack = backStack,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            onBack = backStack::navigateUp,
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            },
            popTransitionSpec = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                    slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            },
            entryProvider = entryProvider {
                val homeRouter = HomeRouterImpl(backStack)
                entry<MainRoute.Home> { HomeScreen(homeRouter) }

                handleCampaignRoutes(backStack, SQLDelightCampaignRepository(dbDriverFactory))
                handlePlayerCharacterRoutes(backStack, RamPlayerCharacterRepository())

                handleSpellRoutes(
                    router = SpellRouterImpl(backStack),
                    spellRepository = JsonSpellRepository(fileReader, currentLocale()),
                    userListRepository = userListRepository,
                )
                handleMagicalItemRoutes(
                    router = MagicalItemRouterImpl(backStack),
                    repository = JsonMagicalItemRepository(fileReader),
                    userListRepository = userListRepository,
                )
                handleCreatureRoutes(
                    router = CreatureRouterImpl(backStack),
                    repository = JsonCreatureRepository(fileReader),
                    userListRepository = userListRepository,
                )

                handleUserListRoutes(UserListRouterImpl(backStack), userListRepository)
            },
        )
    }
}
