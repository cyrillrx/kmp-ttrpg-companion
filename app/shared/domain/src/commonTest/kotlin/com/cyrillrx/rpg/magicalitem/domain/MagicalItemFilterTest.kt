package com.cyrillrx.rpg.magicalitem.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MagicalItemFilterTest {

    @Test
    fun `hasActiveFilters is false for default filter`() {
        val defaultFilter = MagicalItemFilter()
        assertFalse(defaultFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when types are set`() {
        val typesFilter = MagicalItemFilter(types = setOf(MagicalItem.Type.WEAPON))
        assertTrue(typesFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when rarities are set`() {
        val raritiesFilter = MagicalItemFilter(rarities = setOf(MagicalItem.Rarity.RARE))
        assertTrue(raritiesFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is false when only query is set`() {
        val queryFilter = MagicalItemFilter(query = "oath")
        assertFalse(queryFilter.hasActiveFilters)
    }
}
