package com.cyrillrx.rpg.server.campaign

import com.cyrillrx.rpg.server.campaign.domain.Campaign
import com.cyrillrx.rpg.server.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.server.campaign.domain.CampaignRepository
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/campaign")
class CampaignController(
    private val repository: CampaignRepository
) {

    @GetMapping
    fun listCampaigns(@RequestParam(required = false) filter: CampaignFilter?): List<Campaign> {
        return runBlocking { repository.getAll(filter) }
    }

    @GetMapping("/{id}")
    fun getCampaign(@PathVariable id: Long): Campaign? {
        return runBlocking { repository.get(id) }
    }

    @PostMapping
    fun createCampaign(@RequestBody request: CreateCampaignRequest) {
        val (name, ruleSet) = request.validate()

        runBlocking { repository.create(name, ruleSet) }
    }

    @DeleteMapping("/{id}")
    fun deleteCampaign(@PathVariable id: Long) {
        runBlocking { repository.delete(id) }
    }
}