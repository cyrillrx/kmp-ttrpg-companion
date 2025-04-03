package com.cyrillrx.rpg.server.campaign

import com.cyrillrx.rpg.server.campaign.domain.RuleSet
import kotlinx.serialization.Serializable

@Serializable
data class CreateCampaignRequest(
    val name: String,
    val ruleSet: String,
) {
    fun getRuleSetEnum(): RuleSet = RuleSet.valueOf(ruleSet)

    fun validate(): Pair<String, RuleSet> {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }
        if (ruleSet.isBlank()) {
            throw IllegalArgumentException("Rule set cannot be blank")
        }

        val name = name.trim()
        val ruleSet = RuleSet.entries.find { it.name == ruleSet }
            ?: throw IllegalArgumentException("Invalid rule set: '$ruleSet'")
        return Pair(name, ruleSet)
    }
}