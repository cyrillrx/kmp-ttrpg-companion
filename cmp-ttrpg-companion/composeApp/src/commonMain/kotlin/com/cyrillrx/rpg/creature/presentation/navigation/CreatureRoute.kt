package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.creature.data.CreatureEntityRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.CreatureAddToListProvider
import com.cyrillrx.rpg.creature.presentation.CreatureItemProvider
import com.cyrillrx.rpg.creature.presentation.component.CreatureCompactListScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureDetailScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModelFactory
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.component.ListDetailScreen
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface CreatureRoute {

    @Serializable
    data object Compendium : NavKey

    @Serializable
    data class Detail(val creatureId: String) : NavKey

    @Serializable
    data class UserListDetail(val listId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerCreatureRoutes() {
    subclass(CreatureRoute.Compendium::class, CreatureRoute.Compendium.serializer())
    subclass(CreatureRoute.Detail::class, CreatureRoute.Detail.serializer())
    subclass(CreatureRoute.UserListDetail::class, CreatureRoute.UserListDetail.serializer())
}

fun EntryProviderScope<NavKey>.handleCreatureRoutes(
    router: CreatureRouter,
    repository: CreatureRepository,
    userListRepository: UserListRepository,
) {
    entry<CreatureRoute.Compendium> {
        val viewModelFactory = CreatureListViewModelFactory(router, repository)
        val viewModel = viewModel<CreatureListViewModel>(factory = viewModelFactory)
        val addToListProvider = CreatureAddToListProvider(repository, userListRepository)
        // CreatureListScreen(viewModel, router, addToListProvider)
        CreatureCompactListScreen(viewModel, router, addToListProvider)
    }

    entry<CreatureRoute.Detail> { route ->
        val creatureId = route.creatureId
        val viewModelFactory = CreatureDetailViewModelFactory(creatureId, repository)
        val viewModel = viewModel<CreatureDetailViewModel>(key = creatureId, factory = viewModelFactory)
        val addToListProvider = CreatureAddToListProvider(repository, userListRepository)
        CreatureDetailScreen(viewModel, router, addToListProvider)
    }

    entry<CreatureRoute.UserListDetail> { route ->
        val listId = route.listId
        val viewModelFactory = ListDetailViewModelFactory(
            listId = listId,
            userListRepository = userListRepository,
            repository = CreatureEntityRepository(repository),
        )
        val viewModel = viewModel<ListDetailViewModel<Creature>>(key = listId, factory = viewModelFactory)
        val itemProvider = CreatureItemProvider(
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
