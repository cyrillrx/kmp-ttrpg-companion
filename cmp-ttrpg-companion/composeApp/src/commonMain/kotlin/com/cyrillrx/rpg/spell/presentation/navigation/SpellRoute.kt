package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.title_my_spell_lists

interface SpellRoute {
    @Serializable
    data object List

    @Serializable
    data object CardCarousel

    @Serializable
    data class Detail(val spellId: String)

    @Serializable
    data object UserLists

    @Serializable
    data class UserListDetail(val listId: String)
}

fun NavGraphBuilder.handleSpellRoutes(
    navController: NavController,
    spellRepository: SpellRepository,
    userListRepository: UserListRepository,
) {
    composable<SpellRoute.List> {
        val router = SpellRouterImpl(navController)
        val viewModelFactory = SpellListViewModelFactory(router, spellRepository)
        val viewModel = viewModel<SpellListViewModel>(factory = viewModelFactory)

        val bottomSheetProvider = SpellAddToListProvider(
            spellRepository = spellRepository,
            userListRepository = userListRepository,
        )

        SpellListScreen(viewModel, router, bottomSheetProvider)
    }

    composable<SpellRoute.CardCarousel> {
        val router = SpellRouterImpl(navController)
        val viewModelFactory = SpellListViewModelFactory(router, spellRepository)
        val viewModel = viewModel<SpellListViewModel>(factory = viewModelFactory)
        SpellCardCarouselScreen(viewModel, router)
    }

    composable<SpellRoute.Detail> { entry ->
        val router = SpellRouterImpl(navController)
        val spellId = entry.toRoute<SpellRoute.Detail>().spellId
        val viewModel = viewModel<SpellDetailViewModel>(
            factory = SpellDetailViewModelFactory(spellId, spellRepository),
        )

        val bottomSheetProvider = SpellAddToListProvider(
            spellRepository = spellRepository,
            userListRepository = userListRepository,
        )

        SpellCardScreen(viewModel = viewModel, router = router, bottomSheetProvider = bottomSheetProvider)
    }

    composable<SpellRoute.UserLists> {
        val router = UserListRouterImpl(navController)
        val viewModel = viewModel<UserListsViewModel>(
            factory = UserListsViewModelFactory(UserList.Type.SPELL, router, userListRepository),
        )
        UserListsScreen(
            viewModel = viewModel,
            title = stringResource(Res.string.title_my_spell_lists),
        )
    }

    composable<SpellRoute.UserListDetail> { entry ->
        val listId = entry.toRoute<SpellRoute.UserListDetail>().listId
        val viewModel = viewModel<ListDetailViewModel<Spell>>(
            factory = ListDetailViewModelFactory(listId, userListRepository, SpellEntityRepository(spellRepository)),
        )
        val router = SpellRouterImpl(navController)
        ListDetailScreen(
            viewModel = viewModel,
            itemProvider = SpellItemProvider(
                onItemClicked = router::openDetail,
                onEmptyLayoutBtnClicked = { navController.navigate(SpellRoute.List) },
            ),
            onNavigateUp = { navController.navigateUp() },
        )
    }
}
