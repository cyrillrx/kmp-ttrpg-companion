package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemCardCarouselScreen
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemCardScreen
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemListScreen
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModelFactory
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModelFactory
import kotlinx.serialization.Serializable

interface MagicalItemRoute {
    @Serializable
    data object List

    @Serializable
    data object CardCarousel

    @Serializable
    data class Detail(val magicalItemId: String)
}

fun NavGraphBuilder.handleMagicalItemRoutes(navController: NavController, repository: MagicalItemRepository) {
    composable<MagicalItemRoute.List> {
        val router = MagicalItemRouterImpl(navController)
        val viewModelFactory = MagicalItemListViewModelFactory(router, repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        MagicalItemListScreen(viewModel)
    }

    composable<MagicalItemRoute.CardCarousel> {
        val router = MagicalItemRouterImpl(navController)
        val viewModelFactory = MagicalItemListViewModelFactory(router, repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        MagicalItemCardCarouselScreen(viewModel)
    }

    composable<MagicalItemRoute.Detail> { entry ->
        val id = entry.toRoute<MagicalItemRoute.Detail>().magicalItemId
        val viewModel = viewModel<MagicalItemDetailViewModel>(
            factory = MagicalItemDetailViewModelFactory(id, repository),
        )
        MagicalItemCardScreen(viewModel, onNavigateUpClicked = { navController.navigateUp() })
    }
}
