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
        assertTrue(filter.matches(goblin))
        assertTrue(filter.matches(dragon))
    }

    @Test
    fun `filter by matching type matches`() {
        val filter = CreatureFilter(types = setOf(Creature.Type.HUMANOID))
        assertTrue(filter.matches(goblin))
    }

    @Test
    fun `filter by non-matching type does not match`() {
        val filter = CreatureFilter(types = setOf(Creature.Type.DRAGON))
        assertFalse(filter.matches(goblin))
    }

    @Test
    fun `filter by matching challenge rating matches`() {
        val filter = CreatureFilter(challengeRatings = setOf(0.25f))
        assertTrue(filter.matches(goblin))
    }

    @Test
    fun `filter by text query matches name`() {
        val filter = CreatureFilter(query = "goblin")
        assertTrue(filter.matches(goblin))
    }

    @Test
    fun `hasActiveFilters is false for default filter`() {
        assertFalse(CreatureFilter().hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when types are set`() {
        assertTrue(CreatureFilter(types = setOf(Creature.Type.BEAST)).hasActiveFilters)
    }
}
