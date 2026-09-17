package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.HitPoints
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HealthGaugeStateTest {

    private fun gauge(current: Int, max: Int = 12, temporary: Int = 0) =
        HitPoints(current = current, max = max, temporary = temporary).toGaugeState()

    // ─── Fractions ───────────────────────────────────────────────────────────

    @Test
    fun `without temporary hit points the gauge spans the maximum`() {
        val state = gauge(current = 6)

        assertEquals(0.5f, state.currentFraction)
        assertEquals(0.5f, state.missingFraction)
        assertEquals(0f, state.temporaryFraction)
    }

    @Test
    fun `temporary hit points stretch the gauge beyond the maximum`() {
        val state = gauge(current = 6, temporary = 5)

        assertEquals(6f / 17f, state.currentFraction)
        assertEquals(6f / 17f, state.missingFraction)
        assertEquals(5f / 17f, state.temporaryFraction)
    }

    @Test
    fun `the temporary segment starts where the maximum ends`() {
        val state = gauge(current = 6, temporary = 5)

        assertEquals(12f / 17f, state.currentFraction + state.missingFraction)
    }

    @Test
    fun `a full character with no temporary pool fills the whole gauge`() {
        val state = gauge(current = 12)

        assertEquals(1f, state.currentFraction)
        assertEquals(0f, state.missingFraction)
    }

    @Test
    fun `a downed character fills nothing`() {
        val state = gauge(current = 0)

        assertEquals(0f, state.currentFraction)
        assertEquals(1f, state.missingFraction)
    }

    @Test
    fun `hit points above the maximum do not overflow the gauge`() {
        val state = gauge(current = 15)

        assertEquals(1f, state.currentFraction)
        assertEquals(0f, state.missingFraction)
    }

    @Test
    fun `hit points below zero leave the gauge empty`() {
        val state = gauge(current = -3)

        assertEquals(0f, state.currentFraction)
        assertEquals(1f, state.missingFraction)
    }

    @Test
    fun `a zero maximum yields empty fractions instead of dividing by zero`() {
        val state = gauge(current = 0, max = 0)

        assertEquals(0f, state.currentFraction)
        assertEquals(0f, state.missingFraction)
        assertEquals(0f, state.temporaryFraction)
    }

    // ─── Thresholds ──────────────────────────────────────────────────────────

    @Test
    fun `above half the maximum reads as high`() {
        assertEquals(HealthLevel.HIGH, gauge(current = 12).level)
        assertEquals(HealthLevel.HIGH, gauge(current = 7).level)
    }

    @Test
    fun `exactly half the maximum already reads as medium`() {
        assertEquals(HealthLevel.MEDIUM, gauge(current = 6).level)
    }

    @Test
    fun `above a quarter of the maximum reads as medium`() {
        assertEquals(HealthLevel.MEDIUM, gauge(current = 4).level)
    }

    @Test
    fun `exactly a quarter of the maximum already reads as low`() {
        assertEquals(HealthLevel.LOW, gauge(current = 3).level)
    }

    @Test
    fun `a downed character reads as low`() {
        assertEquals(HealthLevel.LOW, gauge(current = 0).level)
    }

    @Test
    fun `the threshold ignores the temporary pool`() {
        assertEquals(HealthLevel.LOW, gauge(current = 2, temporary = 20).level)
    }

    // ─── Labels ──────────────────────────────────────────────────────────────

    @Test
    fun `isDown reports a character at zero hit points`() {
        assertFalse(gauge(current = 1).isDown)
        assertTrue(gauge(current = 0).isDown)
    }

    @Test
    fun `the ratio reads as current over maximum`() {
        assertEquals("6/12", gauge(current = 6).ratio)
    }

    @Test
    fun `the temporary label is signed`() {
        assertEquals("+5", gauge(current = 6, temporary = 5).temporary)
    }

    @Test
    fun `an empty temporary pool has no label`() {
        assertNull(gauge(current = 6).temporary)
    }
}
