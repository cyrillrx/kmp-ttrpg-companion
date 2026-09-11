package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.HitPointAdjustment
import com.cyrillrx.rpg.character.domain.HitPoints
import com.cyrillrx.rpg.character.domain.applying
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HitPointsEditorStateTest {

    private val pool = HitPoints(current = 22, max = 24, temporary = 5)

    private fun editor(
        adjustment: HitPointAdjustment = HitPointAdjustment.DAMAGE,
        input: String = "",
    ) = HitPointsEditorState(hitPoints = pool, adjustment = adjustment, input = input)

    // ─── Input ───────────────────────────────────────────────────────────────

    @Test
    fun `an empty input yields a zero amount`() {
        assertEquals(0, editor().amount)
    }

    @Test
    fun `appending digits builds the amount`() {
        val typed = editor().withDigitAppended(3).withDigitAppended(5)

        assertEquals("35", typed.input)
        assertEquals(35, typed.amount)
    }

    @Test
    fun `a digit beyond the maximum length is ignored`() {
        val typed = editor(input = "999").withDigitAppended(9)

        assertEquals("999", typed.input)
    }

    @Test
    fun `a leading zero is dropped`() {
        assertEquals("", editor().withDigitAppended(0).input)
        assertEquals("5", editor().withDigitAppended(0).withDigitAppended(5).input)
    }

    @Test
    fun `a zero inside the amount is kept`() {
        assertEquals("50", editor().withDigitAppended(5).withDigitAppended(0).input)
    }

    @Test
    fun `backspace removes the last digit`() {
        assertEquals("3", editor(input = "35").withLastDigitRemoved().input)
    }

    @Test
    fun `backspace on an empty input is a no-op`() {
        assertEquals("", editor().withLastDigitRemoved().input)
    }

    @Test
    fun `clear resets the input`() {
        assertEquals("", editor(input = "35").cleared().input)
    }

    @Test
    fun `a shortcut replaces the typed amount`() {
        assertEquals(10, editor(input = "35").withAmount(10).amount)
    }

    @Test
    fun `a shortcut is clamped to the maximum amount`() {
        assertEquals(999, editor().withAmount(5_000).amount)
    }

    @Test
    fun `changing the adjustment keeps the typed amount`() {
        val switched = editor(input = "35").withAdjustment(HitPointAdjustment.HEALING)

        assertEquals("35", switched.input)
        assertEquals(HitPointAdjustment.HEALING, switched.adjustment)
    }

    // ─── Preview ─────────────────────────────────────────────────────────────

    @Test
    fun `the damage preview drains the temporary pool first`() {
        val preview = editor(input = "8").preview

        assertEquals(19, preview.current)
        assertEquals(0, preview.temporary)
    }

    @Test
    fun `the healing preview is capped at the maximum`() {
        assertEquals(24, editor(HitPointAdjustment.HEALING, input = "20").preview.current)
    }

    @Test
    fun `the temporary preview replaces the value`() {
        assertEquals(12, editor(HitPointAdjustment.TEMPORARY, input = "12").preview.temporary)
    }

    @Test
    fun `the preview reports the character as down when damage reaches zero`() {
        assertFalse(editor(input = "8").isPreviewDown)
        assertTrue(editor(input = "99").isPreviewDown)
    }

    /** Guards the promise that what the dialog shows is exactly what the view model writes. */
    @Test
    fun `the preview matches the domain rule for every adjustment and amount`() {
        HitPointAdjustment.entries.forEach { adjustment ->
            listOf(0, 1, 5, 22, 999).forEach { amount ->
                val state = editor(adjustment, input = amount.toString())

                assertEquals(pool.applying(adjustment, amount), state.preview)
            }
        }
    }

    // ─── Apply button ────────────────────────────────────────────────────────

    @Test
    fun `apply is disabled while no amount is typed`() {
        assertFalse(editor().isApplyEnabled)
    }

    @Test
    fun `apply is disabled when the preview leaves the pools unchanged`() {
        val full = HitPointsEditorState(
            hitPoints = pool.copy(current = 24),
            adjustment = HitPointAdjustment.HEALING,
            input = "10",
        )

        assertFalse(full.isApplyEnabled)
    }

    @Test
    fun `apply is enabled once the preview changes the pools`() {
        assertTrue(editor(input = "1").isApplyEnabled)
    }

    // ─── Formatting ──────────────────────────────────────────────────────────

    @Test
    fun `the signed amount is negative for damage and positive for healing`() {
        assertEquals("-35", editor(input = "35").signedAmount)
        assertEquals("+20", editor(HitPointAdjustment.HEALING, input = "20").signedAmount)
    }

    @Test
    fun `the signed amount carries no sign while nothing is typed`() {
        assertEquals("0", editor().signedAmount)
        assertEquals("0", editor(HitPointAdjustment.HEALING).signedAmount)
    }

    @Test
    fun `the signed amount is unsigned for temporary hit points`() {
        assertEquals("12", editor(HitPointAdjustment.TEMPORARY, input = "12").signedAmount)
    }

    @Test
    fun `the ratios read as current over maximum`() {
        val state = editor(input = "99")

        assertEquals("22/24", state.currentRatio)
        assertEquals("0/24", state.previewRatio)
    }
}
