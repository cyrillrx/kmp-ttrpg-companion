package com.cyrillrx.rpg.server.campaign.domain

interface CampaignRepository {
    suspend fun getAll(filter: CampaignFilter?): List<Campaign>
    suspend fun get(id: String): Campaign?
    suspend fun save(campaign: Campaign)
    suspend fun delete(id: String)
}
