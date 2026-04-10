package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.creature.data.CreatureEntityRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.CreatureAddToListProvider
import com.cyrillrx.rpg.creature.presentation.CreatureItemProvider
import com.cyrillrx.rpg.creature.presentation.component.CreatureCompactListScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureDetailScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureListScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModelFactory
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.component.ListDetailScreen
import com.cyrillrx.rpg.userlist.presentation.component.UserListsScreen
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouterImpl
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModelFactory
import com.cyrillrx.rpg.userlist.presentation.viewmodel.UserListsViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.UserListsViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.title_my_bestiary_lists

interface CreatureRoute {
    @Serializable
    data object CompactList : NavKey

    @Serializable
    data object List : NavKey

    @Serializable
    data class Detail(val creatureId: String) : NavKey

    @Serializable
    data object UserLists : NavKey

    @Serializable
    data class UserListDetail(val listId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.declareCreatureRoutes() {
    subclass(CreatureRoute.CompactList::class, CreatureRoute.CompactList.serializer())
    subclass(CreatureRoute.List::class, CreatureRoute.List.serializer())
    subclass(CreatureRoute.Detail::class, CreatureRoute.Detail.serializer())
    subclass(CreatureRoute.UserLists::class, CreatureRoute.UserLists.serializer())
    subclass(CreatureRoute.UserListDetail::class, CreatureRoute.UserListDetail.serializer())
}

fun EntryProviderScope<NavKey>.handleCreatureRoutes(
    backStack: NavBackStack<NavKey>,
    repository: CreatureRepository,
    userListRepository: UserListRepository,
) {
    entry<CreatureRoute.CompactList> {
        val router = CreatureRouterImpl(backStack)
        val viewModelFactory = CreatureListViewModelFactory(router, repository)
        val viewModel = viewModel<CreatureListViewModel>(factory = viewModelFactory)
        CreatureCompactListScreen(viewModel, router)
    }

    entry<CreatureRoute.List> {
        val router = CreatureRouterImpl(backStack)
        val viewModelFactory = CreatureListViewModelFactory(router, repository)
        val viewModel = viewModel<CreatureListViewModel>(factory = viewModelFactory)
        val addToListProvider = CreatureAddToListProvider(repository, userListRepository)
        CreatureListScreen(viewModel, router, addToListProvider)
    }

    entry<CreatureRoute.Detail> { route ->
        val router = CreatureRouterImpl(backStack)
        val viewModelFactory = CreatureDetailViewModelFactory(route.creatureId, repository)
        val viewModel = viewModel<CreatureDetailViewModel>(factory = viewModelFactory)
        val addToListProvider = CreatureAddToListProvider(repository, userListRepository)
        CreatureDetailScreen(viewModel, router, addToListProvider)
    }

    entry<CreatureRoute.UserLists> {
        val router = UserListRouterImpl(backStack)
        val viewModelFactory = UserListsViewModelFactory(
            listType = UserList.Type.CREATURE,
            router = router,
            userListRepository = userListRepository,
        )
        val viewModel = viewModel<UserListsViewModel>(factory = viewModelFactory)
        val title = stringResource(Res.string.title_my_bestiary_lists)
        UserListsScreen(viewModel, router, title)
    }

    entry<CreatureRoute.UserListDetail> { route ->
        val viewModelFactory = ListDetailViewModelFactory(
            listId = route.listId,
            userListRepository = userListRepository,
            repository = CreatureEntityRepository(repository),
        )
        val viewModel = viewModel<ListDetailViewModel<Creature>>(factory = viewModelFactory)
        val router = CreatureRouterImpl(backStack)
        val itemProvider = CreatureItemProvider(
            onItemClicked = router::openDetail,
            onEmptyLayoutBtnClicked = router::openList,
        )
        ListDetailScreen(
            viewModel = viewModel,
            itemProvider = itemProvider,
            onNavigateUp = router::navigateUp,
        )
    }
}
