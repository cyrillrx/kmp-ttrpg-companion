package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MonsterApplyFilterTest {

    private val monsters = SampleMonsterRepository.getAll()

    @Test
    fun `null filter returns the full list`() {
        val result = monsters.applyFilter(null)
        assertEquals(monsters, result)
    }

    @Test
    fun `default filter returns all creatures`() {
        val result = monsters.applyFilter(MonsterFilter())
        assertEquals(monsters.size, result.size)
    }

    @Test
    fun `filter by type keeps only matching creatures`() {
        val result = monsters.applyFilter(MonsterFilter(types = setOf(Monster.Type.HUMANOID)))
        assertTrue(result.all { it.type == Monster.Type.HUMANOID })
        assertEquals(1, result.size)
    }

    @Test
    fun `filter by challenge rating keeps only matching creatures`() {
        val result = monsters.applyFilter(MonsterFilter(challengeRatings = setOf(0.25f)))
        assertTrue(result.all { it.challengeRating == 0.25f })
        assertEquals(2, result.size)
    }

    @Test
    fun `filter by query keeps only matching creatures`() {
        val result = monsters.applyFilter(MonsterFilter(query = "goblin"))
        assertEquals(1, result.size)
        assertEquals("Goblin", result.first().resolveTranslation("en").name)
    }

    @Test
    fun `filter with no match returns empty list`() {
        val result = monsters.applyFilter(MonsterFilter(challengeRatings = setOf(99f)))
        assertTrue(result.isEmpty())
    }
}
