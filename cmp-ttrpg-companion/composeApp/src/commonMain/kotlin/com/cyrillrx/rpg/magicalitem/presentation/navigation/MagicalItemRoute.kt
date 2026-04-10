package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.magicalitem.data.MagicalItemEntityRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemAddToListProvider
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
import rpg_companion.composeapp.generated.resources.title_my_item_lists

interface MagicalItemRoute {
    @Serializable
    data object List : NavKey

    @Serializable
    data object CardCarousel : NavKey

    @Serializable
    data class Detail(val magicalItemId: String) : NavKey

    @Serializable
    data object UserLists : NavKey

    @Serializable
    data class UserListDetail(val listId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.declareMagicalItemRoutes() {
    subclass(MagicalItemRoute.CardCarousel::class, MagicalItemRoute.CardCarousel.serializer())
    subclass(MagicalItemRoute.List::class, MagicalItemRoute.List.serializer())
    subclass(MagicalItemRoute.Detail::class, MagicalItemRoute.Detail.serializer())
    subclass(MagicalItemRoute.UserLists::class, MagicalItemRoute.UserLists.serializer())
    subclass(MagicalItemRoute.UserListDetail::class, MagicalItemRoute.UserListDetail.serializer())
}

fun EntryProviderScope<NavKey>.handleMagicalItemRoutes(
    backStack: NavBackStack<NavKey>,
    repository: MagicalItemRepository,
    userListRepository: UserListRepository,
) {
    entry<MagicalItemRoute.List> {
        val router = MagicalItemRouterImpl(backStack)
        val viewModelFactory = MagicalItemListViewModelFactory(router, repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        val addToListProvider = MagicalItemAddToListProvider(repository, userListRepository)
        MagicalItemListScreen(viewModel, addToListProvider)
    }

    entry<MagicalItemRoute.CardCarousel> {
        val router = MagicalItemRouterImpl(backStack)
        val viewModelFactory = MagicalItemListViewModelFactory(router, repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        MagicalItemCardCarouselScreen(viewModel)
    }

    entry<MagicalItemRoute.Detail> { route ->
        val router = MagicalItemRouterImpl(backStack)
        val viewModelFactory = MagicalItemDetailViewModelFactory(route.magicalItemId, repository)
        val viewModel = viewModel<MagicalItemDetailViewModel>(factory = viewModelFactory)
        val addToListProvider = MagicalItemAddToListProvider(repository, userListRepository)
        MagicalItemCardScreen(viewModel, router, addToListProvider)
    }

    entry<MagicalItemRoute.UserLists> {
        val router = UserListRouterImpl(backStack)
        val viewModelFactory = UserListsViewModelFactory(UserList.Type.MAGICAL_ITEM, router, userListRepository)
        val viewModel = viewModel<UserListsViewModel>(factory = viewModelFactory)
        val screenTitle = stringResource(Res.string.title_my_item_lists)
        UserListsScreen(viewModel = viewModel, title = screenTitle)
    }

    entry<MagicalItemRoute.UserListDetail> { route ->
        val viewModelFactory = ListDetailViewModelFactory(
            listId = route.listId,
            userListRepository = userListRepository,
            repository = MagicalItemEntityRepository(repository),
        )
        val viewModel = viewModel<ListDetailViewModel<MagicalItem>>(factory = viewModelFactory)
        val router = MagicalItemRouterImpl(backStack)
        val itemProvider = MagicalItemItemProvider(
            onItemClicked = router::openDetail,
            onEmptyLayoutBtnClicked = { backStack.add(MagicalItemRoute.List) },
        )
        ListDetailScreen(
            viewModel = viewModel,
            itemProvider = itemProvider,
            onNavigateUp = router::navigateUp,
        )
    }
}
