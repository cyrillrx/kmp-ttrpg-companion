package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.spell.data.JsonSpellRepository
import com.cyrillrx.rpg.spell.presentation.component.AlternativeSpellListScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellListScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellCard
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellBookViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellBookViewModelFactory
import kotlinx.serialization.Serializable

interface SpellRoute {
    @Serializable
    data object List

    @Serializable
    data object AlternativeList

    @Serializable
    data class Detail(val serializedSpell: String)
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
        SpellListScreen(viewModel)
    }

    composable<SpellRoute.AlternativeList> {
        val router = SpellRouterImpl(navController)
        val repository = JsonSpellRepository(fileReader)
        val viewModelFactory = SpellBookViewModelFactory(router, repository)
        val viewModel = viewModel<SpellBookViewModel>(factory = viewModelFactory)
        AlternativeSpellListScreen(viewModel)
    }

    composable<SpellRoute.Detail> { entry ->
        val args = entry.toRoute<SpellRoute.Detail>()
        SpellCard(args.serializedSpell.deserialize())
    }
}
