package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.HitPointAdjustment
import com.cyrillrx.rpg.character.domain.HitPoints
import com.cyrillrx.rpg.character.domain.adjust
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
    fun `a lone zero is kept`() {
        assertEquals("0", editor().withDigitAppended(0).input)
        assertEquals("0", editor(input = "0").withDigitAppended(0).input)
    }

    @Test
    fun `a leading zero is dropped once another digit follows`() {
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
    fun `a shortcut adds to the typed amount`() {
        assertEquals(45, editor(input = "35").withAmountAdded(10).amount)
    }

    @Test
    fun `shortcuts stack onto each other`() {
        assertEquals(17, editor().withAmountAdded(5).withAmountAdded(2).withAmountAdded(10).amount)
    }

    @Test
    fun `a shortcut is clamped to the maximum amount`() {
        assertEquals(999, editor(input = "990").withAmountAdded(50).amount)
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
    fun `the maximum preview caps the current pool when it drops`() {
        val preview = editor(HitPointAdjustment.MAXIMUM, input = "10").preview

        assertEquals(10, preview.max)
        assertEquals(10, preview.current)
    }

    @Test
    fun `apply is disabled while the typed maximum is below one`() {
        assertFalse(editor(HitPointAdjustment.MAXIMUM).isApplyEnabled)
        assertFalse(editor(HitPointAdjustment.MAXIMUM, input = "0").isApplyEnabled)
    }

    @Test
    fun `a blow the character walks away from previews as standing`() {
        assertEquals(PreviewOutcome.STANDING, editor(input = "8").outcome)
    }

    @Test
    fun `a blow that empties the pool previews as down`() {
        assertEquals(PreviewOutcome.DOWN, editor(input = "50").outcome)
    }

    @Test
    fun `an empty keypad previews no outcome on a character already down`() {
        val downed = HitPointsEditorState(
            hitPoints = HitPoints(current = 0, max = 24, temporary = 0),
            adjustment = HitPointAdjustment.DAMAGE,
        )

        assertEquals(PreviewOutcome.NONE, downed.outcome)
    }

    @Test
    fun `a blow whose remainder reaches the maximum previews as lethal`() {
        assertEquals(PreviewOutcome.LETHAL, editor(input = "51").outcome)
    }

    @Test
    fun `only damage can preview as lethal`() {
        assertEquals(PreviewOutcome.STANDING, editor(HitPointAdjustment.HEALING, input = "51").outcome)
        assertEquals(PreviewOutcome.STANDING, editor(HitPointAdjustment.TEMPORARY, input = "51").outcome)
        assertEquals(PreviewOutcome.STANDING, editor(HitPointAdjustment.MAXIMUM, input = "51").outcome)
    }

    /** Guards the promise that what the dialog shows is exactly what the view model writes. */
    @Test
    fun `the preview matches the domain rule for every adjustment and amount`() {
        HitPointAdjustment.entries.forEach { adjustment ->
            listOf(1, 5, 22, 999).forEach { amount ->
                val state = editor(adjustment, input = amount.toString())

                assertEquals(pool.adjust(adjustment, amount), state.preview)
            }
        }
    }

    @Test
    fun `an empty keypad previews no change at all`() {
        HitPointAdjustment.entries.forEach { adjustment ->
            assertEquals(pool, editor(adjustment).preview)
        }
    }

    @Test
    fun `a typed zero clears the temporary pool`() {
        val state = editor(HitPointAdjustment.TEMPORARY, input = "0")

        assertEquals(0, state.preview.temporary)
        assertTrue(state.isApplyEnabled)
    }

    @Test
    fun `a typed zero leaves the other adjustments alone`() {
        assertEquals(pool, editor(HitPointAdjustment.DAMAGE, input = "0").preview)
        assertEquals(pool, editor(HitPointAdjustment.HEALING, input = "0").preview)
        assertEquals(pool, editor(HitPointAdjustment.MAXIMUM, input = "0").preview)
    }

    @Test
    fun `an empty keypad leaves apply disabled on every adjustment`() {
        HitPointAdjustment.entries.forEach { adjustment ->
            assertFalse(editor(adjustment).isApplyEnabled)
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

    @Test
    fun `a killing blow stays applicable on a character already at zero`() {
        val downed = HitPointsEditorState(
            hitPoints = HitPoints(current = 0, max = 24, temporary = 0),
            adjustment = HitPointAdjustment.DAMAGE,
            input = "30",
        )

        assertEquals(downed.hitPoints, downed.preview)
        assertEquals(PreviewOutcome.LETHAL, downed.outcome)
        assertTrue(downed.isApplyEnabled)
    }

    // ─── Shortcuts ───────────────────────────────────────────────────────────

    @Test
    fun `shortcuts show only where the amount reads as a delta`() {
        assertTrue(editor(HitPointAdjustment.DAMAGE).showsShortcuts)
        assertTrue(editor(HitPointAdjustment.HEALING).showsShortcuts)
        assertFalse(editor(HitPointAdjustment.TEMPORARY).showsShortcuts)
        assertFalse(editor(HitPointAdjustment.MAXIMUM).showsShortcuts)
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
    fun `the signed amount is unsigned for temporary and maximum hit points`() {
        assertEquals("12", editor(HitPointAdjustment.TEMPORARY, input = "12").signedAmount)
        assertEquals("40", editor(HitPointAdjustment.MAXIMUM, input = "40").signedAmount)
    }

    @Test
    fun `the ratios read as current over maximum`() {
        val state = HitPointsEditorState(pool.copy(temporary = 0), HitPointAdjustment.DAMAGE, input = "99")

        assertEquals("22/24", state.currentRatio)
        assertEquals("0/24", state.previewRatio)
    }

    @Test
    fun `the ratios spell out the temporary pool once it is in play`() {
        val state = editor(input = "3")

        assertEquals("22/24(+5)", state.currentRatio)
        assertEquals("22/24(+2)", state.previewRatio)
    }

    @Test
    fun `the ratios keep the temporary pool visible once it falls to zero`() {
        val state = editor(input = "8")

        assertEquals("22/24(+5)", state.currentRatio)
        assertEquals("19/24(+0)", state.previewRatio)
    }

    @Test
    fun `the ratios spell out a temporary pool the preview is about to grant`() {
        val state = HitPointsEditorState(pool.copy(temporary = 0), HitPointAdjustment.TEMPORARY, input = "5")

        assertEquals("22/24(+0)", state.currentRatio)
        assertEquals("22/24(+5)", state.previewRatio)
    }
}
