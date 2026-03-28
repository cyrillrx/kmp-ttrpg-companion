package com.cyrillrx.rpg.magicalitem.domain

import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MagicalItemApplyFilterTest {

    private val allItems = SampleMagicalItemRepository.getAll()

    @Test
    fun `null filter returns the full list`() {
        val result = allItems.applyFilter(null)
        assertEquals(allItems, result)
    }

    @Test
    fun `default filter returns all items`() {
        val result = allItems.applyFilter(MagicalItemFilter())
        assertEquals(allItems.size, result.size)
    }

    @Test
    fun `filter by type keeps only matching items`() {
        val result = allItems.applyFilter(MagicalItemFilter(types = setOf(MagicalItem.Type.WEAPON)))
        assertTrue(result.all { it.type == MagicalItem.Type.WEAPON })
        assertEquals(1, result.size)
    }

    @Test
    fun `filter by rarity keeps only matching items`() {
        val result = allItems.applyFilter(MagicalItemFilter(rarities = setOf(MagicalItem.Rarity.RARE)))
        assertTrue(result.all { it.rarity == MagicalItem.Rarity.RARE })
        assertEquals(3, result.size)
    }

    @Test
    fun `filter by query keeps only matching items`() {
        val result = allItems.applyFilter(MagicalItemFilter(query = "fireball"))
        assertEquals(2, result.size)
    }

    @Test
    fun `filter with no match returns empty list`() {
        val result = allItems.applyFilter(MagicalItemFilter(rarities = setOf(MagicalItem.Rarity.LEGENDARY)))
        assertTrue(result.isEmpty())
    }
}
