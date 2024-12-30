package com.cyrillrx.rpg.campaign.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.presentation.component.CampaignDetailScreen
import com.cyrillrx.rpg.campaign.presentation.component.CampaignListScreen
import com.cyrillrx.rpg.campaign.presentation.component.CreateCampaignScreen
import com.cyrillrx.rpg.campaign.presentation.viewmodel.CampaignListViewModel
import com.cyrillrx.rpg.campaign.presentation.viewmodel.CampaignListViewModelFactory
import kotlinx.serialization.Serializable

interface CampaignRoute {
    @Serializable
    data object List

    @Serializable
    data class Detail(val serializedCampaign: String)

    @Serializable
    data object Create
}

fun NavGraphBuilder.handleCampaignRoutes(navController: NavController) {
    composable<CampaignRoute.List> {
        val router = CampaignRouterImpl(navController)
        val viewModelFactory = CampaignListViewModelFactory(router)
        val viewModel = viewModel<CampaignListViewModel>(factory = viewModelFactory)
        CampaignListScreen(viewModel)
    }

    composable<CampaignRoute.Detail> { entry ->
        val args = entry.toRoute<CampaignRoute.Detail>()
        CampaignDetailScreen(args.serializedCampaign.deserialize())
    }

    composable<CampaignRoute.Create> {
        CreateCampaignScreen()
    }
}
