package com.cyrillrx.rpg.campaign.data

import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.domain.RuleSet

class RamCampaignRepository : CampaignRepository {

    private val campaigns = mutableMapOf<String, Campaign>().apply {
        put("1", Campaign("1", "Campaign 1", RuleSet.DND5E))
        put("2", Campaign("2", "Campaign 2", RuleSet.PF2E))
        put("3", Campaign("3", "Campaign 3", RuleSet.VAMPIRE_THE_MASQUERADE_5))
    }

    override suspend fun getAll(): List<Campaign> = campaigns.values.toList()

    override suspend fun filter(query: String): List<Campaign> = campaigns.values.filter { it.name.contains(query) }

    override suspend fun get(id: String): Campaign? = campaigns[id]

    override suspend fun save(campaign: Campaign) {
        campaigns[campaign.id] = campaign
    }

    override suspend fun delete(id: String) {
        campaigns.remove(id)
    }
}
