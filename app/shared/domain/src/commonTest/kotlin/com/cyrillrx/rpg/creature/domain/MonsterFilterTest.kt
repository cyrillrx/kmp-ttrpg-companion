package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MonsterFilterTest {

    @Test
    fun `hasActiveFilters is false for default filter`() {
        val defaultFilter = MonsterFilter()
        assertFalse(defaultFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when types are set`() {
        val typesFilter = MonsterFilter(types = setOf(Monster.Type.BEAST))
        assertTrue(typesFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when challengeRatings are set`() {
        val challengeRatingsFilter = MonsterFilter(challengeRatings = setOf(0.25f))
        assertTrue(challengeRatingsFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is false when only query is set`() {
        val queryFilter = MonsterFilter(query = "goblin")
        assertFalse(queryFilter.hasActiveFilters)
    }
}
