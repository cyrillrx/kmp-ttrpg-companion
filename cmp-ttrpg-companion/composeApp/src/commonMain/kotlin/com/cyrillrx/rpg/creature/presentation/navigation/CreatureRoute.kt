package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.component.CreatureCompactListScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureDetailScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureListScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModelFactory
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModelFactory
import kotlinx.serialization.Serializable

interface CreatureRoute {
    @Serializable
    data object CompactList

    @Serializable
    data object List

    @Serializable
    data class Detail(val creatureId: String)
}

fun NavGraphBuilder.handleCreatureRoutes(navController: NavController, repository: CreatureRepository) {
    composable<CreatureRoute.CompactList> {
        val router = CreatureRouterImpl(navController)
        val viewModelFactory = CreatureListViewModelFactory(router, repository)
        val viewModel = viewModel<CreatureListViewModel>(factory = viewModelFactory)
        CreatureCompactListScreen(viewModel)
    }

    composable<CreatureRoute.List> {
        val router = CreatureRouterImpl(navController)
        val viewModelFactory = CreatureListViewModelFactory(router, repository)
        val viewModel = viewModel<CreatureListViewModel>(factory = viewModelFactory)
        CreatureListScreen(viewModel)
    }

    composable<CreatureRoute.Detail> { entry ->
        val id = entry.toRoute<CreatureRoute.Detail>().creatureId
        val viewModel = viewModel<CreatureDetailViewModel>(
            factory = CreatureDetailViewModelFactory(id, repository),
        )
        val creature by viewModel.item.collectAsState()
        creature?.let {
            CreatureDetailScreen(it, onNavigateUpClicked = { navController.navigateUp() })
        }
    }
}
