package com.cyrillrx.rpg.campaign.domain

class CampaignFilter(
    val query: String = "",
    val ruleSets: Set<RuleSet> = emptySet(),
)
