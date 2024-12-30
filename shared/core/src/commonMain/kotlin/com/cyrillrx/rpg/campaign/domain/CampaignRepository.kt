package com.cyrillrx.rpg.campaign.domain

interface CampaignRepository {
    suspend fun getAll(): List<Campaign>
    suspend fun filter(query: String): List<Campaign>
    suspend fun get(id: String): Campaign?
    suspend fun save(campaign: Campaign)
    suspend fun delete(id: String)
}
