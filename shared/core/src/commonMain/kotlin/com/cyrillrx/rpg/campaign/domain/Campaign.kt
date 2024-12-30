package com.cyrillrx.rpg.campaign.domain

import kotlinx.serialization.Serializable

@Serializable
class Campaign(
    val id: String,
    val name: String,
    val ruleSet: RuleSet,
)
