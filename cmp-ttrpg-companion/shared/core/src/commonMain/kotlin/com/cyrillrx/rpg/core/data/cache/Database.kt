package com.cyrillrx.rpg.core.data.cache

import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.RuleSet

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun getAllCampaigns(): List<Campaign> {
        return dbQuery.selectAllCampaigns(::mapCampaignSelecting).executeAsList()
    }

    fun insertCampaign(campaign: Campaign) {
        dbQuery.insertCampaign(
            id = campaign.id,
            name = campaign.name,
            ruleSet = campaign.ruleSet.ordinal.toLong(),
        )
    }

    fun getCampaign(id: String): Campaign? {
        return dbQuery.selectCampaignById(id, ::mapCampaignSelecting).executeAsOneOrNull()
    }

    fun deleteCampaign(id: String) {
        dbQuery.deleteCampaign(id)
    }

    private fun mapCampaignSelecting(id: String, name: String, ruleSet: Long): Campaign =
        Campaign(id = id, name = name, ruleSet = RuleSet.fromInt(ruleSet.toInt()))

    companion object {
        const val DATABASE_NAME = "ttrpg_companion.db"
    }
}
