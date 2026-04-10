package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp
import com.cyrillrx.rpg.spell.data.SpellEntityRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellAddToListProvider
import com.cyrillrx.rpg.spell.presentation.SpellItemProvider
import com.cyrillrx.rpg.spell.presentation.component.SpellCardCarouselScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellCardScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellListScreen
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModelFactory
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
import rpg_companion.composeapp.generated.resources.title_my_spell_lists

interface SpellRoute {
    @Serializable
    data object List : NavKey

    @Serializable
    data object CardCarousel : NavKey

    @Serializable
    data class Detail(val spellId: String) : NavKey

    @Serializable
    data object UserLists : NavKey

    @Serializable
    data class UserListDetail(val listId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.declareSpellRoutes() {
    subclass(SpellRoute.CardCarousel::class, SpellRoute.CardCarousel.serializer())
    subclass(SpellRoute.List::class, SpellRoute.List.serializer())
    subclass(SpellRoute.Detail::class, SpellRoute.Detail.serializer())
    subclass(SpellRoute.UserLists::class, SpellRoute.UserLists.serializer())
    subclass(SpellRoute.UserListDetail::class, SpellRoute.UserListDetail.serializer())
}

fun EntryProviderScope<NavKey>.handleSpellRoutes(
    backStack: NavBackStack<NavKey>,
    spellRepository: SpellRepository,
    userListRepository: UserListRepository,
) {
    entry<SpellRoute.List> {
        val router = SpellRouterImpl(backStack)
        val viewModelFactory = SpellListViewModelFactory(router, spellRepository)
        val viewModel = viewModel<SpellListViewModel>(factory = viewModelFactory)
        val bottomSheetProvider = SpellAddToListProvider(
            spellRepository = spellRepository,
            userListRepository = userListRepository,
        )
        SpellListScreen(viewModel, router, bottomSheetProvider)
    }

    entry<SpellRoute.CardCarousel> {
        val router = SpellRouterImpl(backStack)
        val viewModelFactory = SpellListViewModelFactory(router, spellRepository)
        val viewModel = viewModel<SpellListViewModel>(factory = viewModelFactory)
        SpellCardCarouselScreen(viewModel, router)
    }

    entry<SpellRoute.Detail> { route ->
        val router = SpellRouterImpl(backStack)
        val viewModelFactory = SpellDetailViewModelFactory(route.spellId, spellRepository)
        val viewModel = viewModel<SpellDetailViewModel>(factory = viewModelFactory)
        val bottomSheetProvider = SpellAddToListProvider(
            spellRepository = spellRepository,
            userListRepository = userListRepository,
        )
        SpellCardScreen(viewModel, router, bottomSheetProvider)
    }

    entry<SpellRoute.UserLists> {
        val router = UserListRouterImpl(backStack)
        val viewModelFactory = UserListsViewModelFactory(
            listType = UserList.Type.SPELL,
            router = router,
            userListRepository = userListRepository,
        )
        val viewModel = viewModel<UserListsViewModel>(factory = viewModelFactory)
        val title = stringResource(Res.string.title_my_spell_lists)
        UserListsScreen(viewModel, router, title)
    }

    entry<SpellRoute.UserListDetail> { route ->
        val viewModelFactory = ListDetailViewModelFactory(
            listId = route.listId,
            userListRepository = userListRepository,
            repository = SpellEntityRepository(spellRepository),
        )
        val viewModel = viewModel<ListDetailViewModel<Spell>>(factory = viewModelFactory)
        val router = SpellRouterImpl(backStack)
        val itemProvider = SpellItemProvider(
            onItemClicked = router::openDetail,
            onEmptyLayoutBtnClicked = router::openList,
        )
        ListDetailScreen(
            viewModel = viewModel,
            itemProvider = itemProvider,
            onNavigateUp = backStack::navigateUp,
        )
    }
}
