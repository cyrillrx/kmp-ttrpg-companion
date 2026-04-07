package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.title_my_bestiary_lists

interface CreatureRoute {
    @Serializable
    data object CompactList

    @Serializable
    data object List

    @Serializable
    data class Detail(val creatureId: String)

    @Serializable
    data object UserLists

    @Serializable
    data class UserListDetail(val listId: String)
}

fun NavGraphBuilder.handleCreatureRoutes(
    navController: NavController,
    repository: CreatureRepository,
    userListRepository: UserListRepository,
) {
    composable<CreatureRoute.CompactList> {
        val router = CreatureRouterImpl(navController)
        val viewModelFactory = CreatureListViewModelFactory(router, repository)
        val viewModel = viewModel<CreatureListViewModel>(factory = viewModelFactory)
        CreatureCompactListScreen(viewModel)
    }

    composable<CreatureRoute.List> {
        val router = CreatureRouterImpl(navController)
        val viewModelFactory = CreatureListViewModelFactory(router, repository)
        val viewModel = viewModel<CreatureListViewModel>(factory = viewModelFactory)
        val addToListProvider = CreatureAddToListProvider(repository, userListRepository)
        CreatureListScreen(viewModel, addToListProvider)
    }

    composable<CreatureRoute.Detail> { entry ->
        val router = CreatureRouterImpl(navController)
        val creatureId = entry.toRoute<CreatureRoute.Detail>().creatureId
        val viewModel = viewModel<CreatureDetailViewModel>(
            factory = CreatureDetailViewModelFactory(creatureId, repository),
        )
        val addToListProvider = CreatureAddToListProvider(repository, userListRepository)
        CreatureDetailScreen(viewModel = viewModel, router = router, addToListProvider = addToListProvider)
    }

    composable<CreatureRoute.UserLists> {
        val router = UserListRouterImpl(navController)
        val viewModel = viewModel<UserListsViewModel>(
            factory = UserListsViewModelFactory(UserList.Type.CREATURE, router, userListRepository),
        )
        UserListsScreen(
            viewModel = viewModel,
            title = stringResource(Res.string.title_my_bestiary_lists),
        )
    }

    composable<CreatureRoute.UserListDetail> { entry ->
        val listId = entry.toRoute<CreatureRoute.UserListDetail>().listId
        val viewModel = viewModel<ListDetailViewModel<Creature>>(
            factory = ListDetailViewModelFactory(listId, userListRepository, CreatureEntityRepository(repository)),
        )
        val router = CreatureRouterImpl(navController)
        ListDetailScreen(
            viewModel = viewModel,
            itemProvider = CreatureItemProvider(
                onItemClicked = router::openDetail,
                onEmptyLayoutBtnClicked = { navController.navigate(CreatureRoute.List) },
            ),
            onNavigateUp = { navController.navigateUp() },
        )
    }
}
