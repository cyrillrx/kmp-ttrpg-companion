package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemAddToListProvider
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemItemProvider
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemCardScreen
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemListScreen
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModelFactory
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.component.ListDetailScreen
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface MagicalItemRoute {
    @Serializable
    data object Compendium : NavKey

    @Serializable
    data class Detail(val magicalItemId: String) : NavKey

    @Serializable
    data class UserListDetail(val listId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerMagicalItemRoutes() {
    subclass(MagicalItemRoute.Compendium::class, MagicalItemRoute.Compendium.serializer())
    subclass(MagicalItemRoute.Detail::class, MagicalItemRoute.Detail.serializer())
    subclass(MagicalItemRoute.UserListDetail::class, MagicalItemRoute.UserListDetail.serializer())
}

fun EntryProviderScope<NavKey>.handleMagicalItemRoutes(
    router: MagicalItemRouter,
    repository: MagicalItemRepository,
    userListRepository: UserListRepository,
) {
    entry<MagicalItemRoute.Compendium> {
        val viewModelFactory = MagicalItemListViewModelFactory(router, repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        val addToListProvider = MagicalItemAddToListProvider(repository, userListRepository)
        MagicalItemListScreen(viewModel, router, addToListProvider)
        // MagicalItemCardCarouselScreen(viewModel, router)
    }

    entry<MagicalItemRoute.Detail> { route ->
        val magicalItemId = route.magicalItemId
        val viewModelFactory = MagicalItemDetailViewModelFactory(magicalItemId, repository)
        val viewModel = viewModel<MagicalItemDetailViewModel>(key = magicalItemId, factory = viewModelFactory)
        val addToListProvider = MagicalItemAddToListProvider(repository, userListRepository)
        MagicalItemCardScreen(viewModel, router, addToListProvider)
    }

    entry<MagicalItemRoute.UserListDetail> { route ->
        val listId = route.listId
        val viewModelFactory = ListDetailViewModelFactory(
            listId = listId,
            userListRepository = userListRepository,
            repository = repository,
        )
        val viewModel = viewModel<ListDetailViewModel<MagicalItem>>(key = listId, factory = viewModelFactory)
        val itemProvider = MagicalItemItemProvider(
            onItemClicked = router::openDetail,
            onEmptyLayoutBtnClicked = router::openCompendium,
        )
        ListDetailScreen(
            viewModel = viewModel,
            itemProvider = itemProvider,
            onNavigateUp = router::navigateUp,
        )
    }
}
