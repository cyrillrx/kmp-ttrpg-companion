package com.cyrillrx.rpg.usercollection.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.component.UserCollectionsScreen
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.UserCollectionsViewModel
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.UserCollectionsViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.title_bestiary_collections
import rpg_companion.composeapp.generated.resources.title_item_collections
import rpg_companion.composeapp.generated.resources.title_spell_collections

interface UserCollectionRoute {
    @Serializable
    data object Spell : NavKey

    @Serializable
    data object MagicalItem : NavKey

    @Serializable
    data object Creature : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerUserCollectionRoutes() {
    subclass(UserCollectionRoute.Spell::class, UserCollectionRoute.Spell.serializer())
    subclass(UserCollectionRoute.MagicalItem::class, UserCollectionRoute.MagicalItem.serializer())
    subclass(UserCollectionRoute.Creature::class, UserCollectionRoute.Creature.serializer())
}

fun EntryProviderScope<NavKey>.handleUserCollectionRoutes(
    router: UserCollectionRouter,
    userCollectionRepository: UserCollectionRepository,
) {
    entry<UserCollectionRoute.Spell> {
        val collectionType = UserCollection.Type.SPELL
        val viewModelFactory = UserCollectionsViewModelFactory(collectionType, userCollectionRepository)
        val viewModel = viewModel<UserCollectionsViewModel>(
            key = collectionType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_spell_collections)
        UserCollectionsScreen(viewModel, router, title)
    }

    entry<UserCollectionRoute.MagicalItem> {
        val collectionType = UserCollection.Type.MAGICAL_ITEM
        val viewModelFactory = UserCollectionsViewModelFactory(collectionType, userCollectionRepository)
        val viewModel = viewModel<UserCollectionsViewModel>(
            key = collectionType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_item_collections)
        UserCollectionsScreen(viewModel, router, title)
    }

    entry<UserCollectionRoute.Creature> {
        val collectionType = UserCollection.Type.MONSTER
        val viewModelFactory = UserCollectionsViewModelFactory(collectionType, userCollectionRepository)
        val viewModel = viewModel<UserCollectionsViewModel>(
            key = collectionType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_bestiary_collections)
        UserCollectionsScreen(viewModel, router, title)
    }
}
