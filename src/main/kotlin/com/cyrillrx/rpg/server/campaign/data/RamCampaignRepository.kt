package com.cyrillrx.rpg.server.campaign.data

import com.cyrillrx.rpg.server.campaign.domain.Campaign
import com.cyrillrx.rpg.server.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.server.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.server.campaign.domain.RuleSet

class RamCampaignRepository : CampaignRepository {

    private val campaigns = mutableListOf<Campaign>().apply {
        add(Campaign("1", "Campaign 1", RuleSet.DND5E))
        add(Campaign("2", "Campaign 2", RuleSet.PATHFINDER_2E))
        add(Campaign("3", "Campaign 3", RuleSet.VAMPIRE_THE_MASQUERADE_5E))
    }

    override suspend fun getAll(filter: CampaignFilter?): List<Campaign> {
        filter ?: return campaigns

        val query = filter.query

        return campaigns.filter {
            filter.ruleSets.contains(it.ruleSet) &&
                    (query.isNotBlank() && it.name.contains(query))
        }
    }

    override suspend fun get(id: String): Campaign? = campaigns.find { it.id == id }

    override suspend fun save(campaign: Campaign) {
        campaigns.add(campaign)
    }

    override suspend fun delete(id: String) {
        campaigns.removeAll { it.id == id }
    }
}
