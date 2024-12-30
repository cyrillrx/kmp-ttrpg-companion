package com.cyrillrx.rpg.campaign.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.campaign.domain.Campaign

interface CampaignRouter {
    fun openCampaignDetail(campaign: Campaign)
    fun openCreateCampaign()
}

class CampaignRouterImpl(private val navController: NavController) : CampaignRouter {
    override fun openCreateCampaign() {
        navController.navigate(CampaignRoute.Create)
    }

    override fun openCampaignDetail(campaign: Campaign) {
        navController.navigate(CampaignRoute.Detail(campaign.serialize()))
    }
}
