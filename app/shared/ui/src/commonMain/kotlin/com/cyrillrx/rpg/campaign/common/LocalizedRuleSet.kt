package com.cyrillrx.rpg.campaign.common

import com.cyrillrx.rpg.campaign.domain.RuleSet

data class LocalizedRuleSet(
    val value: RuleSet,
    val localizedName: String,
)
