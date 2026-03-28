package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreatureFilterTest {

    private val goblin = SampleCreatureRepository.goblin()
    private val dragon = SampleCreatureRepository.youngRedDragon()

    @Test
    fun `default filter matches any creature`() {
        val filter = CreatureFilter()
        assertTrue(goblin.matches(filter))
        assertTrue(dragon.matches(filter))
    }

    @Test
    fun `filter by matching type matches`() {
        val filter = CreatureFilter(types = setOf(Creature.Type.HUMANOID))
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `filter by non-matching type does not match`() {
        val filter = CreatureFilter(types = setOf(Creature.Type.DRAGON))
        assertFalse(goblin.matches(filter))
    }

    @Test
    fun `filter by matching challenge rating matches`() {
        val filter = CreatureFilter(challengeRatings = setOf(0.25f))
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `filter by non-matching challenge rating does not match`() {
        val filter = CreatureFilter(challengeRatings = setOf(99f))
        assertFalse(goblin.matches(filter))
    }

    @Test
    fun `filter by text query matches title`() {
        val filter = CreatureFilter(query = "goblin")
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `filter by text query matches title with different case`() {
        val filter = CreatureFilter(query = "GOBLIN")
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `filter by text query matches description`() {
        val filter = CreatureFilter(query = "black-hearted")
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `hasActiveFilters is false for default filter`() {
        val defaultCreatureFilter = CreatureFilter()
        assertFalse(defaultCreatureFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when types are set`() {
        val beastFilter = CreatureFilter(types = setOf(Creature.Type.BEAST))
        assertTrue(beastFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when challengeRatings are set`() {
        val challengeRatingsFilter = CreatureFilter(challengeRatings = setOf(0.25f))
        assertTrue(challengeRatingsFilter.hasActiveFilters)
    }
}
