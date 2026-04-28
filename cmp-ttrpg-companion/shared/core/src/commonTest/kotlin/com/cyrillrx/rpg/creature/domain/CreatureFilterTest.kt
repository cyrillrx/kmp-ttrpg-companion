package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreatureFilterTest {

    @Test
    fun `hasActiveFilters is false for default filter`() {
        val defaultFilter = CreatureFilter()
        assertFalse(defaultFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when types are set`() {
        val typesFilter = CreatureFilter(types = setOf(Monster.Type.BEAST))
        assertTrue(typesFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when challengeRatings are set`() {
        val challengeRatingsFilter = CreatureFilter(challengeRatings = setOf(0.25f))
        assertTrue(challengeRatingsFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is false when only query is set`() {
        val queryFilter = CreatureFilter(query = "goblin")
        assertFalse(queryFilter.hasActiveFilters)
    }
}
