package com.cyrillrx.rpg.campaign.create.viewmodel

sealed class CreateCampaignError {
    data object EmptyCampaignName : CreateCampaignError()
    data object UndefinedRuleSet : CreateCampaignError()
    data object CampaignAlreadyExists : CreateCampaignError()
}
