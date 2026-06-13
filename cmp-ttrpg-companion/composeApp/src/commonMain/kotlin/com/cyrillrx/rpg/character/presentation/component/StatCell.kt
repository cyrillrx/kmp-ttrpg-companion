package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium

/**
 * Reusable stat cell used by ability, saving throw and combat sections: an [ElevatedCard] showing a
 * bold [value] under a muted [label], with an optional [caption] line and an optional click action.
 */
@Composable
internal fun StatCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Unspecified,
    caption: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val cardContent: @Composable ColumnScope.() -> Unit = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor,
            )
            if (caption != null) {
                Text(
                    text = caption,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
    if (onClick == null) {
        ElevatedCard(modifier = modifier, content = cardContent)
    } else {
        ElevatedCard(onClick = onClick, modifier = modifier, content = cardContent)
    }
}
