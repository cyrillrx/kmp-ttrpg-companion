package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.dieTileMinHeight
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMedium
import com.cyrillrx.rpg.core.presentation.theme.iconSizeSmall
import com.cyrillrx.rpg.core.presentation.theme.removeDieButtonSize
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.KeepRule
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.btn_advantage
import rpg_companion.app.shared.ui.generated.resources.btn_disadvantage
import rpg_companion.app.shared.ui.generated.resources.btn_remove_die_of
import rpg_companion.app.shared.ui.generated.resources.die_label

@Composable
internal fun D20Row(
    onDisadvantageClicked: () -> Unit,
    onD20Clicked: () -> Unit,
    onAdvantageClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val d20 = stringResource(Res.string.die_label, Die.D20.sides)

    Row(horizontalArrangement = Arrangement.spacedBy(spacingMedium), modifier = modifier.height(IntrinsicSize.Min)) {
        DieTile(
            label = d20,
            caption = stringResource(Res.string.btn_disadvantage),
            onClick = onDisadvantageClicked,
            accent = KeepRule.LOWER.color(),
            modifier = Modifier.weight(1f),
        )
        DieTile(
            label = d20,
            caption = "",
            onClick = onD20Clicked,
            modifier = Modifier.weight(1f),
        )
        DieTile(
            label = d20,
            caption = stringResource(Res.string.btn_advantage),
            onClick = onAdvantageClicked,
            accent = KeepRule.HIGHER.color(),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
internal fun DieTile(
    label: String,
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: Color? = null,
    onRemove: (() -> Unit)? = null,
    icon: ImageVector? = null,
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(borderWidth, (accent ?: MaterialTheme.colorScheme.outline).copy(alpha = borderAlpha)),
        colors = dieTileColors(accent),
        // A hovered card gains a shadow, which shows through the transparent advantage tiles.
        elevation = CardDefaults.outlinedCardElevation(),
        modifier = modifier
            .fillMaxHeight()
            .heightIn(min = dieTileMinHeight),
    ) {
        Box(modifier = Modifier.fillMaxSize().heightIn(min = dieTileMinHeight)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(spacingSmall),
            ) {
                if (icon == null) {
                    Text(text = label, style = MaterialTheme.typography.titleLarge)
                } else {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(iconSizeMedium))
                }

                Text(
                    text = caption,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (accent == null) MaterialTheme.colorScheme.tertiary else Color.Unspecified,
                )
            }

            onRemove?.let { remove ->
                IconButton(
                    onClick = remove,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(removeDieButtonSize),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = stringResource(Res.string.btn_remove_die_of, label),
                        modifier = Modifier.size(iconSizeSmall),
                    )
                }
            }
        }
    }
}

@Composable
private fun dieTileColors(accent: Color?): CardColors = if (accent == null) {
    CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
} else {
    CardDefaults.cardColors(containerColor = Color.Transparent, contentColor = accent)
}

@Preview
@Composable
private fun PreviewD20RowLight() {
    AppThemePreview(darkTheme = false) {
        D20Row(onDisadvantageClicked = {}, onD20Clicked = {}, onAdvantageClicked = {})
    }
}

@Preview
@Composable
private fun PreviewD20RowDark() {
    AppThemePreview(darkTheme = true) { D20Row(onDisadvantageClicked = {}, onD20Clicked = {}, onAdvantageClicked = {}) }
}
