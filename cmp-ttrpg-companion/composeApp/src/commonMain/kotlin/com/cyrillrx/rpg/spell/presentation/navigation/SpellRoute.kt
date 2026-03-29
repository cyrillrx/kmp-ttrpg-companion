package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.component.MySpellListsScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellCardCarouselScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellCardScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellListScreen
import com.cyrillrx.rpg.spell.presentation.viewmodel.MySpellListsViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.MySpellListsViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.serialization.Serializable

interface SpellRoute {
    @Serializable
    data object List

    @Serializable
    data object CardCarousel

    @Serializable
    data class Detail(val spellId: String)

    @Serializable
    data object MyLists
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
            onNavigateUpClicked = { navController.navigateUp() },
        )
    }

    composable<SpellRoute.MyLists> {
        val router = SpellRouterImpl(navController)
        val viewModel = viewModel<MySpellListsViewModel>(
            factory = MySpellListsViewModelFactory(router, userListRepository),
        )
        MySpellListsScreen(viewModel)
    }

}
