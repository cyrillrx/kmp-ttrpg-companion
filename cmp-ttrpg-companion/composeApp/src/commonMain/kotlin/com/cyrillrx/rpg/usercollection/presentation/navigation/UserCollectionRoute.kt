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
import rpg_companion.composeapp.generated.resources.title_my_bestiary_lists
import rpg_companion.composeapp.generated.resources.title_my_item_lists
import rpg_companion.composeapp.generated.resources.title_my_spell_lists

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
        val listType = UserCollection.Type.SPELL
        val viewModelFactory = UserCollectionsViewModelFactory(listType, userCollectionRepository)
        val viewModel = viewModel<UserCollectionsViewModel>(
            key = listType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_my_spell_lists)
        UserCollectionsScreen(viewModel, router, title)
    }

    entry<UserCollectionRoute.MagicalItem> {
        val listType = UserCollection.Type.MAGICAL_ITEM
        val viewModelFactory = UserCollectionsViewModelFactory(listType, userCollectionRepository)
        val viewModel = viewModel<UserCollectionsViewModel>(
            key = listType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_my_item_lists)
        UserCollectionsScreen(viewModel, router, title)
    }

    entry<UserCollectionRoute.Creature> {
        val listType = UserCollection.Type.MONSTER
        val viewModelFactory = UserCollectionsViewModelFactory(listType, userCollectionRepository)
        val viewModel = viewModel<UserCollectionsViewModel>(
            key = listType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_my_bestiary_lists)
        UserCollectionsScreen(viewModel, router, title)
    }
}
