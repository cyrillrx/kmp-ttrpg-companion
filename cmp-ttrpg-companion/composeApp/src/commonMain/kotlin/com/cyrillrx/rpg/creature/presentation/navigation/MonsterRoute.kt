package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.presentation.MonsterAddToCollectionProvider
import com.cyrillrx.rpg.creature.presentation.MonsterItemProvider
import com.cyrillrx.rpg.creature.presentation.component.MonsterDetailScreen
import com.cyrillrx.rpg.creature.presentation.component.MonsterListScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterDetailViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterDetailViewModelFactory
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterListViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterListViewModelFactory
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.component.CollectionDetailScreen
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.CollectionDetailViewModel
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.CollectionDetailViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface MonsterRoute {

    @Serializable
    data object Compendium : NavKey

    @Serializable
    data class Detail(val monsterId: String) : NavKey

    @Serializable
    data class UserCollectionDetail(val listId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerMonsterRoutes() {
    subclass(MonsterRoute.Compendium::class, MonsterRoute.Compendium.serializer())
    subclass(MonsterRoute.Detail::class, MonsterRoute.Detail.serializer())
    subclass(MonsterRoute.UserCollectionDetail::class, MonsterRoute.UserCollectionDetail.serializer())
}

fun EntryProviderScope<NavKey>.handleMonsterRoutes(
    router: MonsterRouter,
    repository: MonsterRepository,
    userCollectionRepository: UserCollectionRepository,
) {
    entry<MonsterRoute.Compendium> {
        val viewModelFactory = MonsterListViewModelFactory(repository)
        val viewModel = viewModel<MonsterListViewModel>(factory = viewModelFactory)
        val addToCollectionProvider = MonsterAddToCollectionProvider(repository, userCollectionRepository)
        MonsterListScreen(viewModel, router, addToCollectionProvider)
    }

    entry<MonsterRoute.Detail> { route ->
        val monsterId = route.monsterId
        val viewModelFactory = MonsterDetailViewModelFactory(monsterId, repository)
        val viewModel = viewModel<MonsterDetailViewModel>(key = monsterId, factory = viewModelFactory)
        val addToCollectionProvider = MonsterAddToCollectionProvider(repository, userCollectionRepository)
        MonsterDetailScreen(viewModel, router, addToCollectionProvider)
    }

    entry<MonsterRoute.UserCollectionDetail> { route ->
        val listId = route.listId
        val viewModelFactory = CollectionDetailViewModelFactory(
            listId = listId,
            userCollectionRepository = userCollectionRepository,
            repository = repository,
        )
        val viewModel = viewModel<CollectionDetailViewModel<Monster>>(key = listId, factory = viewModelFactory)
        val itemProvider = MonsterItemProvider(
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
