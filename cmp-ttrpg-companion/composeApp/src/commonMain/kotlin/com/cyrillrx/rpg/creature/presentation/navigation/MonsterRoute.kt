package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.creature.data.MonsterEntityRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.presentation.MonsterAddToListProvider
import com.cyrillrx.rpg.creature.presentation.MonsterItemProvider
import com.cyrillrx.rpg.creature.presentation.component.MonsterDetailScreen
import com.cyrillrx.rpg.creature.presentation.component.MonsterListScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterDetailViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterDetailViewModelFactory
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterListViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterListViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.component.ListDetailScreen
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface MonsterRoute {

    @Serializable
    data object Compendium : NavKey

    @Serializable
    data class Detail(val creatureId: String) : NavKey

    @Serializable
    data class UserListDetail(val listId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerMonsterRoutes() {
    subclass(MonsterRoute.Compendium::class, MonsterRoute.Compendium.serializer())
    subclass(MonsterRoute.Detail::class, MonsterRoute.Detail.serializer())
    subclass(MonsterRoute.UserListDetail::class, MonsterRoute.UserListDetail.serializer())
}

fun EntryProviderScope<NavKey>.handleMonsterRoutes(
    router: MonsterRouter,
    repository: MonsterRepository,
    userListRepository: UserListRepository,
) {
    entry<MonsterRoute.Compendium> {
        val viewModelFactory = MonsterListViewModelFactory(router, repository)
        val viewModel = viewModel<MonsterListViewModel>(factory = viewModelFactory)
        val addToListProvider = MonsterAddToListProvider(repository, userListRepository)
        MonsterListScreen(viewModel, router, addToListProvider)
    }

    entry<MonsterRoute.Detail> { route ->
        val creatureId = route.creatureId
        val viewModelFactory = MonsterDetailViewModelFactory(creatureId, repository)
        val viewModel = viewModel<MonsterDetailViewModel>(key = creatureId, factory = viewModelFactory)
        val addToListProvider = MonsterAddToListProvider(repository, userListRepository)
        MonsterDetailScreen(viewModel, router, addToListProvider)
    }

    entry<MonsterRoute.UserListDetail> { route ->
        val listId = route.listId
        val viewModelFactory = ListDetailViewModelFactory(
            listId = listId,
            userListRepository = userListRepository,
            repository = MonsterEntityRepository(repository),
        )
        val viewModel = viewModel<ListDetailViewModel<Monster>>(key = listId, factory = viewModelFactory)
        val itemProvider = MonsterItemProvider(
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
