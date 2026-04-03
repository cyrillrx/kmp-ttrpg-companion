package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.rpg.magicalitem.data.MagicalItemEntityRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemHeaderProvider
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemItemProvider
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemCardCarouselScreen
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemCardScreen
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemListScreen
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModelFactory
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModelFactory
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
import rpg_companion.composeapp.generated.resources.error_while_loading_magical_items
import rpg_companion.composeapp.generated.resources.title_my_item_lists

interface MagicalItemRoute {
    @Serializable
    data object List

    @Serializable
    data object CardCarousel

    @Serializable
    data class Detail(val magicalItemId: String)

    @Serializable
    data object UserLists

    @Serializable
    data class UserListDetail(val listId: String)

    @Serializable
    data class AddToList(val magicalItemId: String)
}

fun NavGraphBuilder.handleMagicalItemRoutes(
    navController: NavController,
    repository: MagicalItemRepository,
    userListRepository: UserListRepository,
) {
    composable<MagicalItemRoute.List> {
        val router = MagicalItemRouterImpl(navController)
        val viewModelFactory = MagicalItemListViewModelFactory(router, repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        MagicalItemListScreen(viewModel)
    }

    composable<MagicalItemRoute.CardCarousel> {
        val router = MagicalItemRouterImpl(navController)
        val viewModelFactory = MagicalItemListViewModelFactory(router, repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        MagicalItemCardCarouselScreen(viewModel)
    }

    composable<MagicalItemRoute.Detail> { entry ->
        val router = MagicalItemRouterImpl(navController)
        val id = entry.toRoute<MagicalItemRoute.Detail>().magicalItemId
        val viewModel = viewModel<MagicalItemDetailViewModel>(
            factory = MagicalItemDetailViewModelFactory(id, repository),
        )
        MagicalItemCardScreen(viewModel = viewModel, router = router)
    }

    composable<MagicalItemRoute.AddToList> { entry ->
        val magicalItemId = entry.toRoute<MagicalItemRoute.AddToList>().magicalItemId
        val viewModel = viewModel<AddToListViewModel<MagicalItem>>(
            factory = AddToListViewModelFactory(
                itemId = magicalItemId,
                listType = UserList.Type.MAGICAL_ITEM,
                userListRepository = userListRepository,
                repository = MagicalItemEntityRepository(repository),
                errorMessage = Res.string.error_while_loading_magical_items,
            ),
        )
        AddToListScreen(
            viewModel = viewModel,
            headerProvider = MagicalItemHeaderProvider(),
            onNavigateUp = { navController.navigateUp() },
        )
    }

    composable<MagicalItemRoute.UserLists> {
        val router = UserListRouterImpl(navController)
        val viewModel = viewModel<UserListsViewModel>(
            factory = UserListsViewModelFactory(UserList.Type.MAGICAL_ITEM, router, userListRepository),
        )
        UserListsScreen(
            viewModel = viewModel,
            title = stringResource(Res.string.title_my_item_lists),
        )
    }

    composable<MagicalItemRoute.UserListDetail> { entry ->
        val listId = entry.toRoute<MagicalItemRoute.UserListDetail>().listId
        val viewModel = viewModel<ListDetailViewModel<MagicalItem>>(
            factory = ListDetailViewModelFactory(listId, userListRepository, MagicalItemEntityRepository(repository)),
        )
        val router = MagicalItemRouterImpl(navController)
        ListDetailScreen(
            viewModel = viewModel,
            itemProvider = MagicalItemItemProvider(onItemClicked = router::openDetail),
            onNavigateUp = { navController.navigateUp() },
        )
    }
}
