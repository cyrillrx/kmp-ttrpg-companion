package com.cyrillrx.rpg.campaign.data

import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory

class SQLDelightCampaignRepository(databaseDriverFactory: DatabaseDriverFactory) : CampaignRepository {

    private val database = Database(databaseDriverFactory)

    override suspend fun getAll(): List<Campaign> = database.getAllCampaigns()

    override suspend fun filter(query: String): List<Campaign> = getAll().filter { it.name.contains(query) }

    override suspend fun get(id: String): Campaign? = database.getCampaign(id)

    override suspend fun save(campaign: Campaign) {
        database.insertCampaign(campaign)
    }

    override suspend fun delete(id: String) {
        database.deleteCampaign(id)
    }
}
