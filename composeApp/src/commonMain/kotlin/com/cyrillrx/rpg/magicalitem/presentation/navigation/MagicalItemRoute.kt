package com.cyrillrx.rpg.magicalitem.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.core.data.ComposeFileReader
import com.cyrillrx.rpg.magicalitem.data.JsonMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemCard
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemListScreen
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModel
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModelFactory
import kotlinx.serialization.Serializable

interface MagicalItemRoute {
    @Serializable
    data object List

    @Serializable
    data class Detail(val serializedItem: String)
}

fun NavGraphBuilder.handleMagicalItemRoutes(navController: NavController, fileReader: ComposeFileReader) {
    composable<MagicalItemRoute.List> {
        val router = MagicalItemRouterImpl(navController)
        val repository = JsonMagicalItemRepository(fileReader)
        val viewModelFactory = MagicalItemListViewModelFactory(router, repository)
        val viewModel = viewModel<MagicalItemListViewModel>(factory = viewModelFactory)
        MagicalItemListScreen(viewModel)
    }

    composable<MagicalItemRoute.Detail> { entry ->
        val args = entry.toRoute<MagicalItemRoute.Detail>()
        MagicalItemCard(args.serializedItem.deserialize())
    }
}
