package com.cyrillrx.rpg.campaign.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.campaign.create.CreateCampaignScreen
import com.cyrillrx.rpg.campaign.create.viewmodel.CreateCampaignViewModel
import com.cyrillrx.rpg.campaign.create.viewmodel.CreateCampaignViewModelFactory
import com.cyrillrx.rpg.campaign.data.SQLDelightCampaignRepository
import com.cyrillrx.rpg.campaign.detail.CampaignDetailScreen
import com.cyrillrx.rpg.campaign.list.CampaignListScreen
import com.cyrillrx.rpg.campaign.list.viewmodel.CampaignListViewModel
import com.cyrillrx.rpg.campaign.list.viewmodel.CampaignListViewModelFactory
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import kotlinx.serialization.Serializable

interface CampaignRoute {
    @Serializable
    data object List

    @Serializable
    data class Detail(val serializedCampaign: String)

    @Serializable
    data object Create
}

fun NavGraphBuilder.handleCampaignRoutes(navController: NavController, dbDriverFactory: DatabaseDriverFactory) {
    val router = CampaignRouterImpl(navController)
    val repository = SQLDelightCampaignRepository(dbDriverFactory)

    composable<CampaignRoute.List> {
        val viewModelFactory = CampaignListViewModelFactory(router, repository)
        val viewModel = viewModel<CampaignListViewModel>(factory = viewModelFactory)
        CampaignListScreen(viewModel)
    }

    composable<CampaignRoute.Detail> { entry ->
        val args = entry.toRoute<CampaignRoute.Detail>()
        CampaignDetailScreen(
            campaign = args.serializedCampaign.deserialize(),
            onNavigateUpClicked = router::navigateUp,
        )
    }

    composable<CampaignRoute.Create> {
        val viewModelFactory = CreateCampaignViewModelFactory(router, repository)
        val viewModel = viewModel<CreateCampaignViewModel>(factory = viewModelFactory)
        CreateCampaignScreen(viewModel)
    }
}
