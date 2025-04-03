package com.cyrillrx.rpg.campaign.data

import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory

class SQLDelightCampaignRepository(databaseDriverFactory: DatabaseDriverFactory) : CampaignRepository {

    private val database = Database(databaseDriverFactory)

    override suspend fun getAll(filter: CampaignFilter?): List<Campaign> {
        val campaigns = database.getAllCampaigns()
        filter ?: return campaigns

        val query = filter.query
        val ruleSets = filter.ruleSets

        return campaigns.filter {
            (ruleSets.isEmpty() || ruleSets.contains(it.ruleSet)) &&
                (query.isBlank() || it.matches(query))
        }
    }

    private fun Campaign.matches(query: String): Boolean = name.contains(query, ignoreCase = true)

    override suspend fun get(id: String): Campaign? = database.getCampaign(id)

    override suspend fun save(campaign: Campaign) {
        database.insertCampaign(campaign)
    }

    override suspend fun delete(id: String) {
        database.deleteCampaign(id)
    }
}
