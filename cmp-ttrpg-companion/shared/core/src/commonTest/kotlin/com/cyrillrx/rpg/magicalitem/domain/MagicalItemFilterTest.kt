package com.cyrillrx.rpg.magicalitem.domain

import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MagicalItemFilterTest {

    private val oathAxe = SampleMagicalItemRepository.oathAxe()
    private val healingPotion = SampleMagicalItemRepository.healingPotion()

    @Test
    fun `default filter matches any item`() {
        val filter = MagicalItemFilter()
        assertTrue(filter.matches(oathAxe))
        assertTrue(filter.matches(healingPotion))
    }

    @Test
    fun `filter by matching type matches`() {
        val filter = MagicalItemFilter(types = setOf(MagicalItem.Type.WEAPON))
        assertTrue(filter.matches(oathAxe))
    }

    @Test
    fun `filter by non-matching type does not match`() {
        val filter = MagicalItemFilter(types = setOf(MagicalItem.Type.POTION))
        assertFalse(filter.matches(oathAxe))
    }

    @Test
    fun `filter by matching rarity matches`() {
        val filter = MagicalItemFilter(rarities = setOf(MagicalItem.Rarity.RARE))
        assertTrue(filter.matches(oathAxe))
    }

    @Test
    fun `filter by non-matching rarity does not match`() {
        val filter = MagicalItemFilter(rarities = setOf(MagicalItem.Rarity.COMMON))
        assertFalse(filter.matches(oathAxe))
    }

    @Test
    fun `filter by text query matches title`() {
        val filter = MagicalItemFilter(query = "oath")
        assertTrue(filter.matches(oathAxe))
    }

    @Test
    fun `filter by text query matches title with different case`() {
        val filter = MagicalItemFilter(query = "OATH")
        assertTrue(filter.matches(oathAxe))
    }

    @Test
    fun `filter by text query matches description`() {
        val filter = MagicalItemFilter(query = "command word")
        assertTrue(filter.matches(oathAxe))
    }

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
