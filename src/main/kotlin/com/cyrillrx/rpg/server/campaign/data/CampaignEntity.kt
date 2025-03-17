package com.cyrillrx.rpg.server.campaign.data

import com.cyrillrx.rpg.server.campaign.domain.Campaign
import com.cyrillrx.rpg.server.campaign.domain.RuleSet
import jakarta.persistence.*

@Entity
@Table(name = "campaign", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
class CampaignEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_set", nullable = false)
    val ruleSet: RuleSet,
) {
    constructor(name: String, ruleSet: RuleSet) : this(
        id = 0,
        name = name,
        ruleSet = ruleSet,
    )

    fun toDomain(): Campaign = Campaign(
        id = id,
        name = name,
        ruleSet = ruleSet,
    )

    companion object {
        fun fromDomain(campaign: Campaign): CampaignEntity = CampaignEntity(
            id = campaign.id,
            name = campaign.name,
            ruleSet = campaign.ruleSet,
        )
    }
}