package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.spell.data.JsonSpellRepository
import com.cyrillrx.rpg.spell.presentation.component.SpellCardCarouselScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellCardScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellListScreen
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellBookViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellBookViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModelFactory
import kotlinx.serialization.Serializable

interface SpellRoute {
    @Serializable
    data object List

    @Serializable
    data object CardCarousel

    @Serializable
    data class Detail(val spellId: String)
}

fun NavGraphBuilder.handleSpellRoutes(navController: NavController, fileReader: ComposeFileReader) {
    composable<SpellRoute.List>(
        enterTransition = { slideInHorizontally { initialOffset -> initialOffset } },
        exitTransition = { slideOutHorizontally { initialOffset -> initialOffset } },
    ) {
        val router = SpellRouterImpl(navController)
        val repository = JsonSpellRepository(fileReader)
        val viewModelFactory = SpellBookViewModelFactory(router, repository)
        val viewModel = viewModel<SpellBookViewModel>(factory = viewModelFactory)
        SpellListScreen(viewModel, router)
    }

    composable<SpellRoute.CardCarousel> {
        val router = SpellRouterImpl(navController)
        val repository = JsonSpellRepository(fileReader)
        val viewModelFactory = SpellBookViewModelFactory(router, repository)
        val viewModel = viewModel<SpellBookViewModel>(factory = viewModelFactory)
        SpellCardCarouselScreen(viewModel, router)
    }

    composable<SpellRoute.Detail> { entry ->
        val id = entry.toRoute<SpellRoute.Detail>().spellId
        val repository = JsonSpellRepository(fileReader)
        val viewModel = viewModel<SpellDetailViewModel>(
            factory = SpellDetailViewModelFactory(id, repository),
        )
        val spell by viewModel.item.collectAsState()
        spell?.let {
            SpellCardScreen(it, onNavigateUpClicked = { navController.navigateUp() })
        }
    }
}
