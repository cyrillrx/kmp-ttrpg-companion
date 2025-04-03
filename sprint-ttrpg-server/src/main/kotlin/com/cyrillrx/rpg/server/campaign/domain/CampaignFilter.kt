package com.cyrillrx.rpg.server.campaign.domain

class CampaignFilter(
    val query: String = "",
    val ruleSets: Set<RuleSet> = emptySet(),
)