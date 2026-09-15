package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CharacterHitPointsExtTest {

    private val pool = HitPoints(current = 22, max = 24, temporary = 5)

    // ─── Damage ──────────────────────────────────────────────────────────────

    @Test
    fun `damage below the temporary pool only erodes the temporary pool`() {
        val damaged = pool.takeDamage(3)

        assertEquals(22, damaged.current)
        assertEquals(2, damaged.temporary)
    }

    @Test
    fun `damage above the temporary pool spills over to the current pool`() {
        val damaged = pool.takeDamage(8)

        assertEquals(19, damaged.current)
        assertEquals(0, damaged.temporary)
    }

    @Test
    fun `damage exceeding every pool floors the current hit points at zero`() {
        val damaged = pool.takeDamage(100)

        assertEquals(0, damaged.current)
        assertEquals(0, damaged.temporary)
    }

    @Test
    fun `damage of zero leaves the pools untouched`() {
        assertEquals(pool, pool.takeDamage(0))
    }

    @Test
    fun `negative damage is treated as none`() {
        assertEquals(pool, pool.takeDamage(-10))
    }

    // ─── Healing ─────────────────────────────────────────────────────────────

    @Test
    fun `healing is capped at the maximum hit points`() {
        assertEquals(24, pool.heal(20).current)
    }

    @Test
    fun `healing does not restore temporary hit points`() {
        assertEquals(5, pool.heal(20).temporary)
    }

    @Test
    fun `healing brings a downed character back above zero`() {
        val downed = pool.copy(current = 0, temporary = 0)

        assertEquals(7, downed.heal(7).current)
    }

    // ─── Temporary hit points ────────────────────────────────────────────────

    @Test
    fun `setting temporary hit points replaces the current value`() {
        assertEquals(3, pool.setTemporaryHitPoints(3).temporary)
    }

    @Test
    fun `setting temporary hit points to zero clears them`() {
        assertEquals(0, pool.setTemporaryHitPoints(0).temporary)
    }

    @Test
    fun `setting temporary hit points leaves the current pool untouched`() {
        assertEquals(22, pool.setTemporaryHitPoints(12).current)
    }

    // ─── Maximum hit points ──────────────────────────────────────────────────

    @Test
    fun `lowering the maximum caps the current hit points`() {
        val lowered = pool.setMaxHitPoints(10)

        assertEquals(10, lowered.max)
        assertEquals(10, lowered.current)
    }

    @Test
    fun `raising the maximum does not heal`() {
        val raised = pool.setMaxHitPoints(40)

        assertEquals(40, raised.max)
        assertEquals(22, raised.current)
    }

    @Test
    fun `the maximum is coerced into the valid range`() {
        assertEquals(1, pool.setMaxHitPoints(0).max)
        assertEquals(999, pool.setMaxHitPoints(5_000).max)
    }

    // ─── Dispatch ────────────────────────────────────────────────────────────

    @Test
    fun `adjust dispatches to the dedicated rule of every adjustment`() {
        assertEquals(pool.takeDamage(8), pool.adjust(HitPointAdjustment.DAMAGE, 8))
        assertEquals(pool.heal(8), pool.adjust(HitPointAdjustment.HEALING, 8))
        assertEquals(pool.setTemporaryHitPoints(8), pool.adjust(HitPointAdjustment.TEMPORARY, 8))
        assertEquals(pool.setMaxHitPoints(8), pool.adjust(HitPointAdjustment.MAXIMUM, 8))
    }

    @Test
    fun `adjusting a maximum below one leaves the pools unchanged`() {
        assertEquals(pool, pool.adjust(HitPointAdjustment.MAXIMUM, 0))
        assertEquals(pool, pool.adjust(HitPointAdjustment.MAXIMUM, -5))
    }

    @Test
    fun `isDown reports a character at zero hit points`() {
        assertFalse(pool.isDown)
        assertTrue(pool.copy(current = 0).isDown)
    }

    // ─── Character adapters ──────────────────────────────────────────────────

    @Test
    fun `hitPoints reads the three pools of a character`() {
        val character = SampleCharacterRepository.humanFighter().copy(currentHitPoints = 4, temporaryHitPoints = 2)

        assertEquals(HitPoints(current = 4, max = 12, temporary = 2), character.hitPoints)
    }

    @Test
    fun `adjustHitPoints mirrors the pool rule on the character`() {
        val character = SampleCharacterRepository.humanFighter().copy(currentHitPoints = 10, temporaryHitPoints = 3)

        val damaged = character.adjustHitPoints(HitPointAdjustment.DAMAGE, 5)

        assertEquals(8, damaged.currentHitPoints)
        assertEquals(0, damaged.temporaryHitPoints)
    }

    @Test
    fun `adjustHitPoints caps the current hit points when the maximum drops`() {
        val character = SampleCharacterRepository.humanFighter().copy(currentHitPoints = 12)

        val updated = character.adjustHitPoints(HitPointAdjustment.MAXIMUM, 6)

        assertEquals(6, updated.maxHitPoints)
        assertEquals(6, updated.currentHitPoints)
    }
}
