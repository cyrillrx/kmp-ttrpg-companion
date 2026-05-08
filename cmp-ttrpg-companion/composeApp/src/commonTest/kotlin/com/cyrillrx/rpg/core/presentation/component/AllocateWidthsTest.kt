package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AllocateWidthsTest {

    @Test
    fun `preferred fit - returns preferred unchanged (compact keeps natural widths)`() {
        val minimums = listOf(50.dp, 100.dp)
        val preferred = listOf(100.dp, 200.dp)
        val result = allocateWidths(minimums, preferred, available = 400.dp)
        assertEquals(preferred, result)
    }

    @Test
    fun `preferred exactly fills available - returns preferred`() {
        val minimums = listOf(50.dp, 100.dp)
        val preferred = listOf(100.dp, 200.dp)
        val result = allocateWidths(minimums, preferred, available = 300.dp)
        assertEquals(preferred, result)
    }

    @Test
    fun `narrow column keeps its header minimum when data column is very wide`() {
        // Mirrors the "1d8 | Rayon" scenario: one column has a very long single-line cell.
        // "1d8" minimum = 40dp (header), preferred = 40dp (no data wider than header).
        // "Rayon" minimum = 80dp, preferred = 2000dp (paragraph-length cell).
        val minimums = listOf(40.dp, 80.dp)
        val preferred = listOf(40.dp, 2000.dp)
        val result = allocateWidths(minimums, preferred, available = 700.dp)

        // "1d8" has no extra desire (preferred == minimum) so it gets exactly its minimum.
        assertDpApprox(40.dp, result[0])
        // "Rayon" gets all remaining space: 700 - 40 = 660dp.
        assertDpApprox(660.dp, result[1])
        assertDpApprox(700.dp, result[0] + result[1])
    }

    @Test
    fun `satisfied column does not steal space from column with extra desire`() {
        // Column A is already at its natural single-line width (preferred == minimum).
        // Column B still has unmet content desire.
        // Column A must not grow beyond its minimum; Column B absorbs all surplus.
        val minimums = listOf(60.dp, 80.dp)
        val preferred = listOf(60.dp, 500.dp)
        val result = allocateWidths(minimums, preferred, available = 400.dp)

        assertDpApprox(60.dp, result[0])
        assertDpApprox(340.dp, result[1])
        assertDpApprox(400.dp, result[0] + result[1])
    }

    @Test
    fun `remaining space distributed proportionally to extra desire`() {
        // minimums = [50, 50], preferred = [200, 300], available = 400dp
        // totalMin = 100, remaining = 300
        // extras = [150, 250], totalExtra = 400
        // col0 = 50 + 300 * 150/400 = 162.5dp
        // col1 = 50 + 300 * 250/400 = 237.5dp
        val minimums = listOf(50.dp, 50.dp)
        val preferred = listOf(200.dp, 300.dp)
        val result = allocateWidths(minimums, preferred, available = 400.dp)

        assertDpApprox(162.5.dp, result[0])
        assertDpApprox(237.5.dp, result[1])
        assertDpApprox(400.dp, result[0] + result[1])
    }

    @Test
    fun `symmetric columns share remaining space equally`() {
        val minimums = listOf(50.dp, 50.dp)
        val preferred = listOf(200.dp, 200.dp)
        val result = allocateWidths(minimums, preferred, available = 300.dp)

        assertDpApprox(150.dp, result[0])
        assertDpApprox(150.dp, result[1])
        assertDpApprox(300.dp, result[0] + result[1])
    }

    @Test
    fun `minimums dont fit - scales minimums proportionally`() {
        // scale = 300 / 600 = 0.5 → [100, 200]
        val minimums = listOf(200.dp, 400.dp)
        val preferred = listOf(300.dp, 600.dp)
        val result = allocateWidths(minimums, preferred, available = 300.dp)

        assertDpApprox(100.dp, result[0])
        assertDpApprox(200.dp, result[1])
        assertDpApprox(300.dp, result[0] + result[1])
    }

    @Test
    fun `four columns - no column shrinks below minimum and sum equals available`() {
        val minimums = listOf(30.dp, 30.dp, 30.dp, 30.dp)
        val preferred = listOf(80.dp, 500.dp, 120.dp, 60.dp)
        val available = 400.dp
        val result = allocateWidths(minimums, preferred, available)

        assertEquals(4, result.size)
        result.zip(minimums).forEach { (actual, min) ->
            assertTrue(actual.value >= min.value - 0.01f, "Column $actual shrank below minimum $min")
        }
        assertDpApprox(available, result.fold(0.dp) { acc, dp -> acc + dp })
    }

    private fun assertDpApprox(expected: Dp, actual: Dp, tolerance: Float = 0.1f) {
        assertTrue(
            abs(expected.value - actual.value) < tolerance,
            "Expected ~${expected} but was ${actual}",
        )
    }
}
