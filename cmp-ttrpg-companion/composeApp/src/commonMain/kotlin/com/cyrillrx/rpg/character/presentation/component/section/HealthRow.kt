package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cyrillrx.rpg.character.domain.MIN_HIT_POINTS
import com.cyrillrx.rpg.character.presentation.component.StatCell
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.badge_down
import rpg_companion.composeapp.generated.resources.label_current_hp
import rpg_companion.composeapp.generated.resources.label_max_hp
import rpg_companion.composeapp.generated.resources.label_temp_hp

@Composable
internal fun HealthRow(
    currentHitPoints: Int,
    maxHitPoints: Int,
    temporaryHitPoints: Int,
    onCurrentHitPointsTapped: () -> Unit,
    onMaxHitPointsTapped: () -> Unit,
    onTemporaryHitPointsTapped: () -> Unit,
) {
    val isDown = currentHitPoints <= MIN_HIT_POINTS
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = Modifier.fillMaxWidth(),
    ) {
        StatCell(
            label = stringResource(Res.string.label_current_hp),
            value = currentHitPoints.toString(),
            valueColor = if (isDown) MaterialTheme.colorScheme.error else Color.Unspecified,
            caption = if (isDown) stringResource(Res.string.badge_down) else null,
            onClick = onCurrentHitPointsTapped,
            modifier = Modifier.weight(1f),
        )
        StatCell(
            label = stringResource(Res.string.label_max_hp),
            value = maxHitPoints.toString(),
            onClick = onMaxHitPointsTapped,
            modifier = Modifier.weight(1f),
        )
        StatCell(
            label = stringResource(Res.string.label_temp_hp),
            value = temporaryHitPoints.toString(),
            onClick = onTemporaryHitPointsTapped,
            modifier = Modifier.weight(1f),
        )
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview
@Composable
private fun PreviewHealthRowLight() {
    AppThemePreview(darkTheme = false) { HealthRowPreview() }
}

@Preview
@Composable
private fun PreviewHealthRowDark() {
    AppThemePreview(darkTheme = true) { HealthRowPreview() }
}

@Composable
private fun HealthRowPreview() {
    HealthRow(
        currentHitPoints = 22,
        maxHitPoints = 24,
        temporaryHitPoints = 5,
        onCurrentHitPointsTapped = {},
        onMaxHitPointsTapped = {},
        onTemporaryHitPointsTapped = {},
    )
}
