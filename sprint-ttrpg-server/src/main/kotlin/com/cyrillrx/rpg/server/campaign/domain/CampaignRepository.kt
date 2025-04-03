package com.cyrillrx.rpg.server.campaign.domain

interface CampaignRepository {
    fun getAll(filter: CampaignFilter?): List<Campaign>
    fun get(id: Long): Campaign?
    fun create(name: String, ruleSet: RuleSet)
    fun save(campaign: Campaign)
    fun delete(id: Long)
}