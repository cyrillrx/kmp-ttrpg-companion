package com.cyrillrx.rpg.server.campaign.data

import com.cyrillrx.rpg.server.campaign.domain.Campaign
import com.cyrillrx.rpg.server.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.server.campaign.domain.RuleSet
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@DataJpaTest
class HibernateCampaignRepositoryTest {

    @TestConfiguration
    class HibernateCampaignRepositoryTestConfiguration {
        @Bean
        fun hibernateCampaignRepository(entityManager: EntityManager): HibernateCampaignRepository {
            return HibernateCampaignRepository(entityManager)
        }
    }

    @Autowired
    private lateinit var repository: HibernateCampaignRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `getAll returns empty list when no campaigns exist`() {
        val result = repository.getAll(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAll filters campaigns by name query`() {
        val campaign1 = CampaignEntity(name = "Adventure", ruleSet = RuleSet.UNDEFINED)
        entityManager.persist(campaign1)

        val campaign2 = CampaignEntity(name = "Mystery", ruleSet = RuleSet.UNDEFINED)
        entityManager.persist(campaign2)

        entityManager.flush()

        val filter = CampaignFilter(query = "adv", ruleSets = emptySet())
        val result = repository.getAll(filter)

        assertEquals(1, result.size)
        assertEquals("Adventure", result.first().name)
    }

    @Test
    fun `getAll filters campaigns by rule set`() {
        val campaign1 = CampaignEntity(name = "Adventure", ruleSet = RuleSet.UNDEFINED)
        entityManager.persist(campaign1)

        val campaign2 = CampaignEntity(name = "Mystery", ruleSet = RuleSet.CALL_OF_CTHULHU_7E)
        entityManager.persist(campaign2)

        entityManager.flush()

        val filter = CampaignFilter(query = "", ruleSets = setOf(RuleSet.CALL_OF_CTHULHU_7E))
        val result = repository.getAll(filter)

        assertEquals(1, result.size)
        assertEquals("Mystery", result.first().name)
    }

    @Test
    fun `get returns campaign by id`() {
        val campaign = CampaignEntity(name = "Adventure", ruleSet = RuleSet.UNDEFINED)
        entityManager.persist(campaign)
        entityManager.flush()

        val result = repository.get(campaign.id)
        assertNotNull(result)
        assertEquals("Adventure", result?.name)
    }

    @Test
    fun `get returns null for non-existent campaign`() {
        val result = repository.get(999L)
        assertNull(result)
    }

    @Test
    fun `create saves a new campaign`() {
        repository.create(name = "New Campaign", ruleSet = RuleSet.UNDEFINED)

        val query = entityManager.createQuery(
            "SELECT c FROM CampaignEntity c WHERE c.name = :name",
            CampaignEntity::class.java
        )
        query.setParameter("name", "New Campaign")
        val result = query.resultList

        assertEquals(1, result.size)
        assertEquals("New Campaign", result.first().name)
    }

    @Test
    fun `save updates an existing campaign`() {
        val campaign = CampaignEntity(name = "Old Name", ruleSet = RuleSet.UNDEFINED)
        entityManager.persist(campaign)
        entityManager.flush()

        val updatedCampaign = Campaign(
            id = campaign.id,
            name = "Updated Name",
            ruleSet = RuleSet.UNDEFINED,
        )

        repository.save(updatedCampaign)

        val updatedEntity = entityManager.find(CampaignEntity::class.java, campaign.id)
        assertNotNull(updatedEntity)
        assertEquals("Updated Name", updatedEntity.name)
    }

    @Test
    fun `delete removes campaign by id`() {
        val campaign = CampaignEntity(name = "To Be Deleted", ruleSet = RuleSet.UNDEFINED)
        entityManager.persist(campaign)
        entityManager.flush()

        repository.delete(campaign.id)

        val deletedEntity = entityManager.find(CampaignEntity::class.java, campaign.id)
        assertNull(deletedEntity)
    }
}