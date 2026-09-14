package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.cyrillrx.rpg.character.presentation.HealthGaugeState
import com.cyrillrx.rpg.character.presentation.HealthLevel
import com.cyrillrx.rpg.core.presentation.theme.LocalHealthColors
import com.cyrillrx.rpg.core.presentation.theme.gaugeHeight

@Composable
internal fun HealthGauge(
    state: HealthGaugeState,
    modifier: Modifier = Modifier,
) {
    val remainingFraction = 1f - state.currentFraction - state.temporaryFraction
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(gaugeHeight)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        // A zero weight is not a valid constraint, so an empty segment is left out rather than sized.
        GaugeSegment(state.currentFraction, state.level.color)
        GaugeSegment(state.temporaryFraction, MaterialTheme.colorScheme.tertiary)
        if (remainingFraction > 0f) Spacer(Modifier.weight(remainingFraction))
    }
}

@Composable
private fun RowScope.GaugeSegment(fraction: Float, color: Color) {
    if (fraction <= 0f) return
    Box(
        modifier = Modifier
            .weight(fraction)
            .fillMaxHeight()
            .background(color),
    )
}

internal val HealthLevel.color: Color
    @Composable get() = when (this) {
        HealthLevel.HIGH -> MaterialTheme.colorScheme.primary
        HealthLevel.MEDIUM -> LocalHealthColors.current.warning
        HealthLevel.LOW -> MaterialTheme.colorScheme.error
    }
