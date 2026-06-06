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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.svg.SvgDecoder
import com.cyrillrx.rpg.campaign.data.SQLDelightCampaignRepository
import com.cyrillrx.rpg.campaign.navigation.handleCampaignRoutes
import com.cyrillrx.rpg.campaign.navigation.registerCampaignRoutes
import com.cyrillrx.rpg.character.data.JsonCharacterPresetRepository
import com.cyrillrx.rpg.character.data.SQLDelightCharacterRepository
import com.cyrillrx.rpg.character.presentation.navigation.handleCharacterRoutes
import com.cyrillrx.rpg.character.presentation.navigation.registerCharacterRoutes
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.navigation.navigateUp
import com.cyrillrx.rpg.core.presentation.LocalDistanceUnit
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.creature.data.JsonMonsterRepository
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRouterImpl
import com.cyrillrx.rpg.creature.presentation.navigation.handleMonsterRoutes
import com.cyrillrx.rpg.creature.presentation.navigation.registerMonsterRoutes
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouterImpl
import com.cyrillrx.rpg.magicalitem.data.JsonMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRouterImpl
import com.cyrillrx.rpg.magicalitem.presentation.navigation.handleMagicalItemRoutes
import com.cyrillrx.rpg.magicalitem.presentation.navigation.registerMagicalItemRoutes
import com.cyrillrx.rpg.settings.data.SqlDelightUserPreferencesRepository
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import com.cyrillrx.rpg.settings.presentation.navigation.handleSettingsRoutes
import com.cyrillrx.rpg.settings.presentation.navigation.registerSettingsRoutes
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
            registerMonsterRoutes()

            registerUserListRoutes()

            registerSettingsRoutes()
        }
    }
}

@Composable
@Preview
fun App(dbDriverFactory: DatabaseDriverFactory) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
    val prefsRepository: UserPreferencesRepository = remember(dbDriverFactory) {
        SqlDelightUserPreferencesRepository(dbDriverFactory)
    }
    var prefsInitialized by remember { mutableStateOf(false) }
    LaunchedEffect(prefsRepository) {
        prefsRepository.initialize()
        prefsInitialized = true
    }
    val prefs by prefsRepository.preferences.collectAsState()

    if (!prefsInitialized) return

    AppTheme(theme = prefs.theme) {
        CompositionLocalProvider(LocalDistanceUnit provides prefs.distanceUnit) {
            val backStack = rememberNavBackStack(navSavedStateConfig, MainRoute.Home)

            val fileReader = ComposeFileReader()
            val userListRepository = remember(dbDriverFactory) { SQLDelightUserListRepository(dbDriverFactory) }

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

                    handleCampaignRoutes(
                        backStack = backStack,
                        repository = remember(dbDriverFactory) {
                            SQLDelightCampaignRepository(dbDriverFactory)
                        },
                    )
                    handleCharacterRoutes(
                        backStack = backStack,
                        characterRepository = remember(dbDriverFactory) {
                            SQLDelightCharacterRepository(dbDriverFactory)
                        },
                        pcPresetRepository = JsonCharacterPresetRepository(fileReader, "files/pc-presets.json"),
                        npcPresetRepository = JsonCharacterPresetRepository(fileReader, "files/npc-presets.json"),
                    )
                    handleSpellRoutes(
                        router = SpellRouterImpl(backStack),
                        spellRepository = JsonSpellRepository(fileReader),
                        userListRepository = userListRepository,
                    )
                    handleMagicalItemRoutes(
                        router = MagicalItemRouterImpl(backStack),
                        repository = JsonMagicalItemRepository(fileReader),
                        userListRepository = userListRepository,
                    )
                    handleMonsterRoutes(
                        router = MonsterRouterImpl(backStack),
                        repository = JsonMonsterRepository(fileReader),
                        userListRepository = userListRepository,
                    )
                    handleUserListRoutes(UserListRouterImpl(backStack), userListRepository)
                    handleSettingsRoutes(backStack, prefsRepository)
                },
            )
        } // CompositionLocalProvider
    }
}
