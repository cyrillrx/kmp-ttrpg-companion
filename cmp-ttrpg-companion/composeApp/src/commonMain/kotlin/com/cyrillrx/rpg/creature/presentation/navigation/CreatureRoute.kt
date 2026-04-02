package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.component.CreatureCompactListScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureDetailScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureListDetailScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureListScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModelFactory
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListDetailViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListDetailViewModelFactory
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.component.AddCreatureToListScreen
import com.cyrillrx.rpg.userlist.presentation.component.UserListsScreen
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouterImpl
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddCreatureToListViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddCreatureToListViewModelFactory
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

    @Serializable
    data class AddToList(val creatureId: String)
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
        CreatureListScreen(viewModel)
    }

    composable<CreatureRoute.Detail> { entry ->
        val router = CreatureRouterImpl(navController)
        val id = entry.toRoute<CreatureRoute.Detail>().creatureId
        val viewModel = viewModel<CreatureDetailViewModel>(
            factory = CreatureDetailViewModelFactory(id, repository),
        )
        CreatureDetailScreen(viewModel = viewModel, router = router)
    }

    composable<CreatureRoute.AddToList> { entry ->
        val route = entry.toRoute<CreatureRoute.AddToList>()
        val viewModel = viewModel<AddCreatureToListViewModel>(
            factory = AddCreatureToListViewModelFactory(
                itemId = route.creatureId,
                listType = UserList.Type.CREATURE,
                userListRepository = userListRepository,
                creatureRepository = repository,
            ),
        )
        AddCreatureToListScreen(
            viewModel = viewModel,
            onNavigateUp = { navController.navigateUp() },
        )
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
        val viewModel = viewModel<CreatureListDetailViewModel>(
            factory = CreatureListDetailViewModelFactory(listId, userListRepository, repository),
        )
        CreatureListDetailScreen(
            viewModel = viewModel,
            onNavigateUpClicked = { navController.navigateUp() },
        )
    }
}
