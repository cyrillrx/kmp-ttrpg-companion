package com.cyrillrx.rpg.server.campaign.data

import com.cyrillrx.rpg.server.campaign.domain.Campaign
import com.cyrillrx.rpg.server.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.server.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.server.campaign.domain.RuleSet

class RamCampaignRepository : CampaignRepository {

    private val campaigns = mutableListOf<Campaign>().apply {
        add(Campaign(0L, "Campaign 1", RuleSet.DND5E))
        add(Campaign(1L, "Campaign 2", RuleSet.PATHFINDER_2E))
        add(Campaign(2L, "Campaign 3", RuleSet.VAMPIRE_THE_MASQUERADE_5E))
    }

    override fun getAll(filter: CampaignFilter?): List<Campaign> {
        filter ?: return campaigns

        val query = filter.query

        return campaigns.filter {
            filter.ruleSets.contains(it.ruleSet) &&
                    (query.isNotBlank() && it.name.contains(query))
        }
    }

    override fun get(id: Long): Campaign? = campaigns.find { it.id == id }

    override fun create(name: String, ruleSet: RuleSet) {
        val id = campaigns.size.toLong()
        val campaign = Campaign(id, name, ruleSet)
        campaigns.add(campaign)
    }

    override fun save(campaign: Campaign) {
        campaigns.add(campaign)
    }

    override fun delete(id: Long) {
        campaigns.removeAll { it.id == id }
    }
}
