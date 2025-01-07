package com.cyrillrx.rpg.character.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.character.data.RamPlayerCharacterRepository
import com.cyrillrx.rpg.character.presentation.component.CreatePlayerCharacterScreen
import com.cyrillrx.rpg.character.presentation.component.PlayerCharacterDetailScreen
import com.cyrillrx.rpg.character.presentation.component.PlayerCharacterListScreen
import com.cyrillrx.rpg.character.presentation.viewmodel.PlayerCharacterListViewModel
import com.cyrillrx.rpg.character.presentation.viewmodel.PlayerCharacterListViewModelFactory
import kotlinx.serialization.Serializable

interface PlayerCharacterRoute {
    @Serializable
    data object List

    @Serializable
    data class Detail(val serializedPlayerCharacter: String)

    @Serializable
    data object Create
}

fun NavGraphBuilder.handlePlayerCharacterRoutes(navController: NavController) {
    composable<PlayerCharacterRoute.List> {
        val router = PlayerCharacterRouterImpl(navController)
        val repository = RamPlayerCharacterRepository()
        val viewModelFactory = PlayerCharacterListViewModelFactory(router, repository)
        val viewModel = viewModel<PlayerCharacterListViewModel>(factory = viewModelFactory)
        PlayerCharacterListScreen(viewModel)
    }

    composable<PlayerCharacterRoute.Detail> { entry ->
        val args = entry.toRoute<PlayerCharacterRoute.Detail>()
        PlayerCharacterDetailScreen(
            character = args.serializedPlayerCharacter.deserialize(),
            onNavigateUpClicked = { navController.navigateUp() },
        )
    }

    composable<PlayerCharacterRoute.Create> {
        CreatePlayerCharacterScreen(
            onNavigateUpClicked = { navController.navigateUp() },
        )
    }
}
