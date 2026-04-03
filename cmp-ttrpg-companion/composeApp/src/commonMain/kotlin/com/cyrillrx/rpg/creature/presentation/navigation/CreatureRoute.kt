package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.rpg.creature.data.CreatureEntityRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.CreatureUiProvider
import com.cyrillrx.rpg.creature.presentation.component.CreatureCompactListScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureDetailScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureListScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModelFactory
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.component.AddToListScreen
import com.cyrillrx.rpg.userlist.presentation.component.ListDetailScreen
import com.cyrillrx.rpg.userlist.presentation.component.UserListsScreen
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouterImpl
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddToListViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddToListViewModelFactory
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModelFactory
import com.cyrillrx.rpg.userlist.presentation.viewmodel.UserListsViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.UserListsViewModelFactory
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_creatures
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
        val creatureId = entry.toRoute<CreatureRoute.AddToList>().creatureId
        val viewModel = viewModel<AddToListViewModel<Creature>>(
            factory = AddToListViewModelFactory(
                itemId = creatureId,
                listType = UserList.Type.CREATURE,
                userListRepository = userListRepository,
                repository = CreatureEntityRepository(repository),
                errorMessage = Res.string.error_while_loading_creatures,
            ),
        )
        AddToListScreen(
            viewModel = viewModel,
            headerProvider = CreatureUiProvider(),
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
        val viewModel = viewModel<ListDetailViewModel<Creature>>(
            factory = ListDetailViewModelFactory(listId, userListRepository, CreatureEntityRepository(repository)),
        )
        val router = CreatureRouterImpl(navController)
        ListDetailScreen(
            viewModel = viewModel,
            router = router,
            uiProvider = CreatureUiProvider(),
        )
    }
}
