package com.cyrillrx.rpg.server.campaign

import com.cyrillrx.rpg.server.campaign.data.RamCampaignRepository
import com.cyrillrx.rpg.server.campaign.domain.Campaign
import com.cyrillrx.rpg.server.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.server.campaign.domain.CampaignRepository
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/campaign")
class CampaignController {

    val repository: CampaignRepository = RamCampaignRepository()

    @GetMapping
    fun listCampaigns(@RequestParam(required = false) filter: CampaignFilter?): List<Campaign> {
        return runBlocking { repository.getAll(filter) }
    }

    @GetMapping("/{id}")
    fun getCampaign(@PathVariable id: String): Campaign? {
        return runBlocking { repository.get(id) }
    }

    @PostMapping
    fun createCampaign(@RequestBody campaign: Campaign) {
        runBlocking { repository.save(campaign) }
    }

    @DeleteMapping("/{id}")
    fun deleteCampaign(@PathVariable id: String) {
        runBlocking { repository.delete(id) }
    }
}