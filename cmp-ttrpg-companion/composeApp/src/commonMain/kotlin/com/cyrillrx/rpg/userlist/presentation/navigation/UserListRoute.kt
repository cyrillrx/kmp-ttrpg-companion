package com.cyrillrx.rpg.userlist.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.component.UserListsScreen
import com.cyrillrx.rpg.userlist.presentation.viewmodel.UserListsViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.UserListsViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.title_my_bestiary_lists
import rpg_companion.composeapp.generated.resources.title_my_item_lists
import rpg_companion.composeapp.generated.resources.title_my_spell_lists

interface UserListRoute {
    @Serializable
    data object Spell : NavKey

    @Serializable
    data object MagicalItem : NavKey

    @Serializable
    data object Creature : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerUserListRoutes() {
    subclass(UserListRoute.Spell::class, UserListRoute.Spell.serializer())
    subclass(UserListRoute.MagicalItem::class, UserListRoute.MagicalItem.serializer())
    subclass(UserListRoute.Creature::class, UserListRoute.Creature.serializer())
}

fun EntryProviderScope<NavKey>.handleUserListRoutes(
    router: UserListRouter,
    userListRepository: UserListRepository,
) {
    entry<UserListRoute.Spell> {
        val listType = UserList.Type.SPELL
        val viewModelFactory = UserListsViewModelFactory(listType, router, userListRepository)
        val viewModel = viewModel<UserListsViewModel>(
            key = listType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_my_spell_lists)
        UserListsScreen(viewModel, router, title)
    }

    entry<UserListRoute.MagicalItem> {
        val listType = UserList.Type.MAGICAL_ITEM
        val viewModelFactory = UserListsViewModelFactory(listType, router, userListRepository)
        val viewModel = viewModel<UserListsViewModel>(
            key = listType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_my_item_lists)
        UserListsScreen(viewModel, router, title)
    }

    entry<UserListRoute.Creature> {
        val listType = UserList.Type.CREATURE
        val viewModelFactory = UserListsViewModelFactory(listType, router, userListRepository)
        val viewModel = viewModel<UserListsViewModel>(
            key = listType.name,
            factory = viewModelFactory,
        )
        val title = stringResource(Res.string.title_my_bestiary_lists)
        UserListsScreen(viewModel, router, title)
    }
}
