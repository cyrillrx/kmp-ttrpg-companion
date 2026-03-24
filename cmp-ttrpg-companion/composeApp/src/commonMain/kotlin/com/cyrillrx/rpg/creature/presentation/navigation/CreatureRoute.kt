package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.component.CreatureCompactListScreen
import com.cyrillrx.rpg.creature.presentation.component.CreatureListScreen
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModelFactory
import kotlinx.serialization.Serializable

interface CreatureRoute {
    @Serializable
    data object CompactList

    @Serializable
    data object List

    @Serializable
    data class Detail(val serializedCreature: String)
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
}
