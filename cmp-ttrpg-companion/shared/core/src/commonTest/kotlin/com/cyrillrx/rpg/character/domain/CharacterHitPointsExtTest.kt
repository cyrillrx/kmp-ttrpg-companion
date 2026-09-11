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
        val damaged = pool.withDamageTaken(3)

        assertEquals(22, damaged.current)
        assertEquals(2, damaged.temporary)
    }

    @Test
    fun `damage above the temporary pool spills over to the current pool`() {
        val damaged = pool.withDamageTaken(8)

        assertEquals(19, damaged.current)
        assertEquals(0, damaged.temporary)
    }

    @Test
    fun `damage exceeding every pool floors the current hit points at zero`() {
        val damaged = pool.withDamageTaken(100)

        assertEquals(0, damaged.current)
        assertEquals(0, damaged.temporary)
    }

    @Test
    fun `damage of zero leaves the pools untouched`() {
        assertEquals(pool, pool.withDamageTaken(0))
    }

    @Test
    fun `negative damage is treated as none`() {
        assertEquals(pool, pool.withDamageTaken(-10))
    }

    // ─── Healing ─────────────────────────────────────────────────────────────

    @Test
    fun `healing is capped at the maximum hit points`() {
        assertEquals(24, pool.withHealingApplied(20).current)
    }

    @Test
    fun `healing does not restore temporary hit points`() {
        assertEquals(5, pool.withHealingApplied(20).temporary)
    }

    @Test
    fun `healing brings a downed character back above zero`() {
        val downed = pool.copy(current = 0, temporary = 0)

        assertEquals(7, downed.withHealingApplied(7).current)
    }

    // ─── Temporary hit points ────────────────────────────────────────────────

    @Test
    fun `setting temporary hit points replaces the current value`() {
        assertEquals(3, pool.withTemporaryHitPointsSet(3).temporary)
    }

    @Test
    fun `setting temporary hit points to zero clears them`() {
        assertEquals(0, pool.withTemporaryHitPointsSet(0).temporary)
    }

    @Test
    fun `setting temporary hit points leaves the current pool untouched`() {
        assertEquals(22, pool.withTemporaryHitPointsSet(12).current)
    }

    // ─── Maximum hit points ──────────────────────────────────────────────────

    @Test
    fun `lowering the maximum caps the current hit points`() {
        val lowered = pool.withMaxHitPointsSet(10)

        assertEquals(10, lowered.max)
        assertEquals(10, lowered.current)
    }

    @Test
    fun `raising the maximum does not heal`() {
        val raised = pool.withMaxHitPointsSet(40)

        assertEquals(40, raised.max)
        assertEquals(22, raised.current)
    }

    @Test
    fun `the maximum is coerced into the valid range`() {
        assertEquals(1, pool.withMaxHitPointsSet(0).max)
        assertEquals(999, pool.withMaxHitPointsSet(5_000).max)
    }

    // ─── Dispatch ────────────────────────────────────────────────────────────

    @Test
    fun `applying dispatches to the dedicated rule of every adjustment`() {
        assertEquals(pool.withDamageTaken(8), pool.applying(HitPointAdjustment.DAMAGE, 8))
        assertEquals(pool.withHealingApplied(8), pool.applying(HitPointAdjustment.HEALING, 8))
        assertEquals(pool.withTemporaryHitPointsSet(8), pool.applying(HitPointAdjustment.TEMPORARY, 8))
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
    fun `withHitPointsAdjusted mirrors the pool rule on the character`() {
        val character = SampleCharacterRepository.humanFighter().copy(currentHitPoints = 10, temporaryHitPoints = 3)

        val damaged = character.withHitPointsAdjusted(HitPointAdjustment.DAMAGE, 5)

        assertEquals(8, damaged.currentHitPoints)
        assertEquals(0, damaged.temporaryHitPoints)
    }

    @Test
    fun `withMaxHitPoints caps the current hit points of the character`() {
        val character = SampleCharacterRepository.humanFighter().copy(currentHitPoints = 12)

        val updated = character.withMaxHitPoints(6)

        assertEquals(6, updated.maxHitPoints)
        assertEquals(6, updated.currentHitPoints)
    }
}
