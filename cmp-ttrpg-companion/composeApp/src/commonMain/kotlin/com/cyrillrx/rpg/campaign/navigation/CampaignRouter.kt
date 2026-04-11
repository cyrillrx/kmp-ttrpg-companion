package com.cyrillrx.rpg.campaign.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.core.navigation.navigateUp

interface CampaignRouter {
    fun navigateUp()
    fun openCampaignDetail(campaign: Campaign)
    fun openCreateCampaign()
}

class CampaignRouterImpl(private val backStack: NavBackStack<NavKey>) : CampaignRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCreateCampaign() {
        backStack.add(CampaignRoute.Create)
    }

    override fun openCampaignDetail(campaign: Campaign) {
        backStack.add(CampaignRoute.Detail(campaign.serialize()))
    }
}
