package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreatureFilterTest {

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
