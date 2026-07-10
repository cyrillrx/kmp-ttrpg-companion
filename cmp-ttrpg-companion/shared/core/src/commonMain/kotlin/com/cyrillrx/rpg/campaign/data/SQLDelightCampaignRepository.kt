package com.cyrillrx.rpg.campaign.data

import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SQLDelightCampaignRepository(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CampaignRepository {

    private val database = Database(databaseDriverFactory)

    override suspend fun getAll(filter: CampaignFilter?): List<Campaign> = withContext(ioDispatcher) {
        val campaigns = database.getAllCampaigns()
        if (filter == null) {
            campaigns
        } else {
            val query = filter.query
            val ruleSets = filter.ruleSets
            campaigns.filter {
                (ruleSets.isEmpty() || ruleSets.contains(it.ruleSet)) &&
                    (query.isBlank() || it.matches(query))
            }
        }
    }

    private fun Campaign.matches(query: String): Boolean = name.contains(query, ignoreCase = true)

    override suspend fun get(id: String): Campaign? = withContext(ioDispatcher) { database.getCampaign(id) }

    override suspend fun save(campaign: Campaign) {
        withContext(ioDispatcher) { database.insertCampaign(campaign) }
    }

    override suspend fun delete(id: String) {
        withContext(ioDispatcher) { database.deleteCampaign(id) }
    }
}
