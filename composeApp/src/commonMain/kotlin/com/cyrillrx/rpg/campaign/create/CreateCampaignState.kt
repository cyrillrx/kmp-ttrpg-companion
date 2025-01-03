package com.cyrillrx.rpg.campaign.create

import com.cyrillrx.rpg.campaign.create.viewmodel.CreateCampaignError
import com.cyrillrx.rpg.campaign.domain.RuleSet

data class CreateCampaignState(
    val campaignName: String,
    val selectedRuleSet: RuleSet,
    val error: CreateCampaignError?,
)
