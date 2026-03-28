package com.cyrillrx.rpg.magicalitem.domain

import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MagicalItemMatchesFilterTest {

    private val oathAxe = SampleMagicalItemRepository.oathAxe()
    private val healingPotion = SampleMagicalItemRepository.healingPotion()

    @Test
    fun `default filter matches any item`() {
        val filter = MagicalItemFilter()
        assertTrue(oathAxe.matches(filter))
        assertTrue(healingPotion.matches(filter))
    }

    @Test
    fun `filter by matching type matches`() {
        val filter = MagicalItemFilter(types = setOf(MagicalItem.Type.WEAPON))
        assertTrue(oathAxe.matches(filter))
    }

    @Test
    fun `filter by non-matching type does not match`() {
        val filter = MagicalItemFilter(types = setOf(MagicalItem.Type.POTION))
        assertFalse(oathAxe.matches(filter))
    }

    @Test
    fun `filter by matching rarity matches`() {
        val filter = MagicalItemFilter(rarities = setOf(MagicalItem.Rarity.RARE))
        assertTrue(oathAxe.matches(filter))
    }

    @Test
    fun `filter by non-matching rarity does not match`() {
        val filter = MagicalItemFilter(rarities = setOf(MagicalItem.Rarity.COMMON))
        assertFalse(oathAxe.matches(filter))
    }

    @Test
    fun `filter by text query matches title`() {
        val filter = MagicalItemFilter(query = "oath")
        assertTrue(oathAxe.matches(filter))
    }

    @Test
    fun `filter by text query matches title with different case`() {
        val filter = MagicalItemFilter(query = "OATH")
        assertTrue(oathAxe.matches(filter))
    }

    @Test
    fun `filter by text query matches subtitle`() {
        val filter = MagicalItemFilter(query = "axe")
        assertTrue(oathAxe.matches(filter))
    }

    @Test
    fun `filter by text query matches description`() {
        val filter = MagicalItemFilter(query = "command word")
        assertTrue(oathAxe.matches(filter))
    }
}
