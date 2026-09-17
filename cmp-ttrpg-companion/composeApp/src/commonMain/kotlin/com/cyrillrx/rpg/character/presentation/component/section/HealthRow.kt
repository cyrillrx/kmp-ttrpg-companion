package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.character.domain.HitPoints
import com.cyrillrx.rpg.character.presentation.toGaugeState
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.LocalHealthColors
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.badge_down
import rpg_companion.composeapp.generated.resources.hp_tab_damage
import rpg_companion.composeapp.generated.resources.hp_tab_healing
import rpg_companion.composeapp.generated.resources.label_hit_points

@Composable
internal fun HealthRow(
    hitPoints: HitPoints,
    onGaugeTapped: () -> Unit,
    onDamageTapped: () -> Unit,
    onHealTapped: () -> Unit,
) {
    val state = hitPoints.toGaugeState()

    Column(verticalArrangement = Arrangement.spacedBy(spacingMedium)) {
        Card(
            onClick = onGaugeTapped,
            border = BorderStroke(borderWidth, MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingCommon, vertical = spacingMedium),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(Res.string.label_hit_points),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                    if (state.isDown) {
                        Text(
                            text = stringResource(Res.string.badge_down),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    Text(
                        text = state.ratio,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    state.temporary?.let { temporary ->
                        Text(
                            text = temporary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
                HealthGauge(state)
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth(),
        ) {
            HealthActionButton(
                label = stringResource(Res.string.hp_tab_damage),
                icon = Icons.Filled.HeartBroken,
                color = MaterialTheme.colorScheme.error,
                onClick = onDamageTapped,
                modifier = Modifier.weight(1f),
            )
            HealthActionButton(
                label = stringResource(Res.string.hp_tab_healing),
                icon = Icons.Filled.Healing,
                color = LocalHealthColors.current.heal,
                onClick = onHealTapped,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun HealthActionButton(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
        border = BorderStroke(borderWidth, color.copy(alpha = borderAlpha)),
        modifier = modifier,
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(iconSizeMedium))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = spacingSmall),
        )
    }
}

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
    Column(verticalArrangement = Arrangement.spacedBy(spacingCommon)) {
        listOf(
            HitPoints(current = 12, max = 12),
            HitPoints(current = 6, max = 12, temporary = 5),
            HitPoints(current = 2, max = 12),
            HitPoints(current = 0, max = 12),
        ).forEach { hitPoints ->
            HealthRow(hitPoints = hitPoints, onGaugeTapped = {}, onDamageTapped = {}, onHealTapped = {})
        }
    }
}
