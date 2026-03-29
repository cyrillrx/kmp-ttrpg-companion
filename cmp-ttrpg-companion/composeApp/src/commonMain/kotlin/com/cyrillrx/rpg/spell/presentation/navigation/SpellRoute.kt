package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.component.SpellCardCarouselScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellCardScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellListDetailScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellListScreen
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListDetailViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListDetailViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.UserListsViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.UserListsViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.UserListsScreen
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_my_spell_lists

interface SpellRoute {
    @Serializable
    data object List

    @Serializable
    data object CardCarousel

    @Serializable
    data class Detail(val spellId: String)

    @Serializable
    data object MyLists

    @Serializable
    data class ListDetail(val listId: String)
}

fun NavGraphBuilder.handleSpellRoutes(
    navController: NavController,
    repository: SpellRepository,
    userListRepository: UserListRepository,
) {
    composable<SpellRoute.List>(
        enterTransition = { slideInHorizontally { initialOffset -> initialOffset } },
        exitTransition = { slideOutHorizontally { initialOffset -> initialOffset } },
    ) {
        val router = SpellRouterImpl(navController)
        val viewModelFactory = SpellListViewModelFactory(router, repository)
        val viewModel = viewModel<SpellListViewModel>(factory = viewModelFactory)
        SpellListScreen(viewModel, router)
    }

    composable<SpellRoute.CardCarousel> {
        val router = SpellRouterImpl(navController)
        val viewModelFactory = SpellListViewModelFactory(router, repository)
        val viewModel = viewModel<SpellListViewModel>(factory = viewModelFactory)
        SpellCardCarouselScreen(viewModel, router)
    }

    composable<SpellRoute.Detail> { entry ->
        val id = entry.toRoute<SpellRoute.Detail>().spellId
        val viewModel = viewModel<SpellDetailViewModel>(
            factory = SpellDetailViewModelFactory(id, repository),
        )
        SpellCardScreen(
            viewModel = viewModel,
            userListRepository = userListRepository,
            onNavigateUpClicked = { navController.navigateUp() },
        )
    }

    composable<SpellRoute.MyLists> {
        val router = SpellRouterImpl(navController)
        val viewModel = viewModel<UserListsViewModel>(
            factory = UserListsViewModelFactory(router, userListRepository),
        )
        UserListsScreen(
            viewModel = viewModel,
            title = stringResource(Res.string.btn_my_spell_lists),
        )
    }

    composable<SpellRoute.ListDetail> { entry ->
        val listId = entry.toRoute<SpellRoute.ListDetail>().listId
        val viewModel = viewModel<SpellListDetailViewModel>(
            factory = SpellListDetailViewModelFactory(listId, userListRepository, repository),
        )
        SpellListDetailScreen(
            viewModel = viewModel,
            onNavigateUpClicked = { navController.navigateUp() },
        )
    }
}
