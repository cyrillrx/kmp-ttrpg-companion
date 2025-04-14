package com.cyrillrx.rpg.server.campaign.data

import com.cyrillrx.rpg.server.campaign.domain.Campaign
import com.cyrillrx.rpg.server.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.server.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.server.campaign.domain.RuleSet
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class HibernateCampaignRepository(
    private val entityManager: EntityManager
) : CampaignRepository {

    @Transactional(readOnly = true)
    override fun getAll(filter: CampaignFilter?): List<Campaign> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val query = criteriaBuilder.createQuery(CampaignEntity::class.java)
        val root = query.from(CampaignEntity::class.java)

        if (filter != null) {
            val predicates = mutableListOf<Predicate>()

            if (filter.query.isNotBlank()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%${filter.query.lowercase()}%"
                    )
                )
            }

            if (filter.ruleSets.isNotEmpty()) {
                predicates.add(root.get<Any>("ruleSet").`in`(filter.ruleSets))
            }

            if (predicates.isNotEmpty()) {
                query.where(*predicates.toTypedArray())
            }
        }

        return entityManager.createQuery(query)
            .resultList
            .map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    override fun get(id: Long): Campaign? =
        entityManager.find(CampaignEntity::class.java, id)?.toDomain()

    @Transactional
    override fun create(name: String, ruleSet: RuleSet) {
        val entity = CampaignEntity(name = name, ruleSet = ruleSet)
        entityManager.merge(entity)
    }

    @Transactional
    override fun save(campaign: Campaign) {
        val entity = CampaignEntity.fromDomain(campaign)
        entityManager.merge(entity)
    }

    @Transactional
    override fun delete(id: Long) {
        entityManager.find(CampaignEntity::class.java, id)?.let {
            entityManager.remove(it)
        }
    }
}