package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Allocates column widths within the available space.
// Each column is guaranteed at least its minimum (header width) to prevent header wrapping.
// Remaining space after minimums is distributed proportionally to each column's extra desire.
internal fun allocateWidths(minimums: List<Dp>, preferred: List<Dp>, available: Dp): List<Dp> {
    val totalPreferred = preferred.fold(0.dp) { acc, dp -> acc + dp }
    if (totalPreferred <= available) return preferred

    val totalMinimum = minimums.fold(0.dp) { acc, dp -> acc + dp }
    if (totalMinimum.value >= available.value) {
        val scale = available.value / totalMinimum.value
        return minimums.map { it * scale }
    }

    val remaining = available - totalMinimum
    val extras = preferred.zip(minimums).map { (p, m) -> maxOf(p - m, 0.dp) }
    val totalExtra = extras.fold(0.dp) { acc, dp -> acc + dp }
    // Safety guard: totalExtra is always > 0 in normal flow because preferred >= minimum per
    // column and totalPreferred > totalMinimum. Handles hypothetical caller misuse gracefully.
    return if (totalExtra.value <= 0f) {
        val perColumn = remaining / preferred.size.toFloat()
        minimums.map { it + perColumn }
    } else {
        minimums.zip(extras).map { (min, extra) ->
            min + remaining * (extra.value / totalExtra.value)
        }
    }
}
