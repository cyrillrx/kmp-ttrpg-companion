package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemAddToCollectionProvider
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemItemProvider
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemDetailScreen
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemListScreen
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModelFactory
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModelFactory
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.component.CollectionDetailScreen
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.CollectionDetailViewModel
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.CollectionDetailViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface MagicalItemRoute {
    @Serializable
    data object Compendium : NavKey

    @Serializable
    data class Detail(val magicalItemId: String) : NavKey

    @Serializable
    data class UserCollectionDetail(val collectionId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerMagicalItemRoutes() {
    subclass(MagicalItemRoute.Compendium::class, MagicalItemRoute.Compendium.serializer())
    subclass(MagicalItemRoute.Detail::class, MagicalItemRoute.Detail.serializer())
    subclass(MagicalItemRoute.UserCollectionDetail::class, MagicalItemRoute.UserCollectionDetail.serializer())
}

fun EntryProviderScope<NavKey>.handleMagicalItemRoutes(
    router: MagicalItemRouter,
    repository: MagicalItemRepository,
    userCollectionRepository: UserCollectionRepository,
) {
    entry<MagicalItemRoute.Compendium> {
        val viewModelFactory = MagicalItemListViewModelFactory(repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        val addToCollectionProvider = MagicalItemAddToCollectionProvider(repository, userCollectionRepository)
        MagicalItemListScreen(viewModel, router, addToCollectionProvider)
    }

    entry<MagicalItemRoute.Detail> { route ->
        val magicalItemId = route.magicalItemId
        val viewModelFactory = MagicalItemDetailViewModelFactory(magicalItemId, repository)
        val viewModel = viewModel<MagicalItemDetailViewModel>(key = magicalItemId, factory = viewModelFactory)
        val addToCollectionProvider = MagicalItemAddToCollectionProvider(repository, userCollectionRepository)
        MagicalItemDetailScreen(viewModel, router, addToCollectionProvider)
    }

    entry<MagicalItemRoute.UserCollectionDetail> { route ->
        val collectionId = route.collectionId
        val viewModelFactory = CollectionDetailViewModelFactory(
            collectionId = collectionId,
            userCollectionRepository = userCollectionRepository,
            repository = repository,
        )
        val viewModel =
            viewModel<CollectionDetailViewModel<MagicalItem>>(key = collectionId, factory = viewModelFactory)
        val itemProvider = MagicalItemItemProvider(
            onItemClicked = router::openDetail,
            onEmptyLayoutBtnClicked = router::openCompendium,
        )
        CollectionDetailScreen(
            viewModel = viewModel,
            itemProvider = itemProvider,
            onNavigateUp = router::navigateUp,
        )
    }
}
