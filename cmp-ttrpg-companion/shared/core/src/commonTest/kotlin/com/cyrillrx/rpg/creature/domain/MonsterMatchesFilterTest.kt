package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MonsterMatchesFilterTest {

    private val goblin = SampleMonsterRepository.goblin()
    private val dragon = SampleMonsterRepository.youngRedDragon()

    @Test
    fun `default filter matches any creature`() {
        val filter = MonsterFilter()
        assertTrue(goblin.matches(filter))
        assertTrue(dragon.matches(filter))
    }

    @Test
    fun `filter by matching type matches`() {
        val filter = MonsterFilter(types = setOf(Monster.Type.HUMANOID))
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `filter by non-matching type does not match`() {
        val filter = MonsterFilter(types = setOf(Monster.Type.DRAGON))
        assertFalse(goblin.matches(filter))
    }

    @Test
    fun `filter by matching challenge rating matches`() {
        val filter = MonsterFilter(challengeRatings = setOf(0.25f))
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `filter by non-matching challenge rating does not match`() {
        val filter = MonsterFilter(challengeRatings = setOf(99f))
        assertFalse(goblin.matches(filter))
    }

    @Test
    fun `filter by text query matches title`() {
        val filter = MonsterFilter(query = "goblin")
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `filter by text query matches title with different case`() {
        val filter = MonsterFilter(query = "GOBLIN")
        assertTrue(goblin.matches(filter))
    }

    @Test
    fun `filter by text query matches description`() {
        val filter = MonsterFilter(query = "black-hearted")
        assertTrue(goblin.matches(filter))
    }
}
