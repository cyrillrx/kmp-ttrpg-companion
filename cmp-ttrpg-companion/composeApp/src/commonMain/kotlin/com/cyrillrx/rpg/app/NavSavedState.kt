package com.cyrillrx.rpg.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.cyrillrx.rpg.campaign.navigation.registerCampaignRoutes
import com.cyrillrx.rpg.character.presentation.navigation.registerCharacterRoutes
import com.cyrillrx.rpg.creature.presentation.navigation.registerMonsterRoutes
import com.cyrillrx.rpg.magicalitem.presentation.navigation.registerMagicalItemRoutes
import com.cyrillrx.rpg.settings.presentation.navigation.registerSettingsRoutes
import com.cyrillrx.rpg.spell.presentation.navigation.registerSpellRoutes
import com.cyrillrx.rpg.usercollection.presentation.navigation.registerUserCollectionRoutes
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

internal val navSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(MainRoute.Home::class, MainRoute.Home.serializer())

        registerCampaignRoutes()
        registerCharacterRoutes()

        registerSpellRoutes()
        registerMagicalItemRoutes()
        registerMonsterRoutes()

        registerUserCollectionRoutes()

        registerSettingsRoutes()

        // Routes take their fully-qualified name as polymorphic discriminator, so moving one to
        // another package makes back stacks persisted by an older build undecodable. Fall back to
        // Home so restoring one resets navigation instead of crashing at launch.
        defaultDeserializer { MainRoute.Home.serializer() }
    }
}

internal val navSavedStateConfig = SavedStateConfiguration {
    serializersModule = navSerializersModule
}

/**
 * Back stack for [App], restored from saved state and cleared of entries the fallback above produced.
 */
@Composable
internal fun rememberAppBackStack(): NavBackStack<NavKey> {
    val backStack = rememberNavBackStack(navSavedStateConfig, MainRoute.Home)

    // During composition rather than in an effect: NavDisplay composes the entries in this same
    // pass, and a repeated key crashes it before any effect would get the chance to run.
    remember(backStack) { backStack.resetIfRestoredThroughFallback() }

    return backStack
}

/**
 * Resets a back stack in which [MainRoute.Home] shows up past the first entry.
 *
 * Only the fallback above puts it there, and it fills every slot it answers for with that same key.
 * Navigation 3 keys the state it saves per entry by the key itself, so leaving the repetitions in
 * place would crash the next transition rather than reset navigation the way the fallback intends.
 */
internal fun MutableList<NavKey>.resetIfRestoredThroughFallback() {
    if (drop(1).none { it == MainRoute.Home }) return

    clear()
    add(MainRoute.Home)
}
