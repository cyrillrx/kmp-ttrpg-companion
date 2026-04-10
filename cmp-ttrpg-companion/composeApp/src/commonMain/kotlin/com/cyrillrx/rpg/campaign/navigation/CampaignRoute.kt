package com.cyrillrx.rpg.campaign.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.campaign.create.CreateCampaignScreen
import com.cyrillrx.rpg.campaign.create.viewmodel.CreateCampaignViewModel
import com.cyrillrx.rpg.campaign.create.viewmodel.CreateCampaignViewModelFactory
import com.cyrillrx.rpg.campaign.detail.CampaignDetailScreen
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.list.CampaignListScreen
import com.cyrillrx.rpg.campaign.list.viewmodel.CampaignListViewModel
import com.cyrillrx.rpg.campaign.list.viewmodel.CampaignListViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

sealed interface CampaignRoute {
    @Serializable
    data object List : NavKey

    @Serializable
    data class Detail(val serializedCampaign: String) : NavKey

    @Serializable
    data object Create : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.declareCampaignRoutes() {
    subclass(CampaignRoute.List::class, CampaignRoute.List.serializer())
    subclass(CampaignRoute.Detail::class, CampaignRoute.Detail.serializer())
    subclass(CampaignRoute.Create::class, CampaignRoute.Create.serializer())
}

fun EntryProviderScope<NavKey>.handleCampaignRoutes(backStack: NavBackStack<NavKey>, repository: CampaignRepository) {
    val router = CampaignRouterImpl(backStack)

    entry<CampaignRoute.List> {
        val viewModelFactory = CampaignListViewModelFactory(router, repository)
        val viewModel = viewModel<CampaignListViewModel>(factory = viewModelFactory)
        CampaignListScreen(viewModel)
    }

    entry<CampaignRoute.Detail> { route ->
        CampaignDetailScreen(
            campaign = route.serializedCampaign.deserialize(),
            onNavigateUpClicked = router::navigateUp,
        )
    }

    entry<CampaignRoute.Create> {
        val viewModelFactory = CreateCampaignViewModelFactory(router, repository)
        val viewModel = viewModel<CreateCampaignViewModel>(factory = viewModelFactory)
        CreateCampaignScreen(viewModel)
    }
}
