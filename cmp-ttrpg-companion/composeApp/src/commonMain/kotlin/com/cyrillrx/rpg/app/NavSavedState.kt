package com.cyrillrx.rpg.app

import androidx.navigation3.runtime.NavKey
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
