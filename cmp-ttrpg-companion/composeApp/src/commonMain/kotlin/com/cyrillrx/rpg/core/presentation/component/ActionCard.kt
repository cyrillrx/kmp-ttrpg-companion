package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.ui.tooling.preview.Preview

// TODO(#305): build the outlined variant on AppCard
@Composable
fun ActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    filled: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (filled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (filled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = if (filled) {
            null
        } else {
            BorderStroke(
                width = borderWidth,
                color = MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha),
            )
        },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            modifier = Modifier.padding(spacingCommon),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor,
            )
        }
    }
}

@Composable
private fun ActionCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingCommon),
        modifier = Modifier.padding(spacingCommon),
    ) {
        ActionCard(
            icon = Icons.Filled.Add,
            title = "New Collection",
            subtitle = "Group entries you want at hand",
            onClick = {},
            filled = true,
        )
        ActionCard(
            icon = Icons.Filled.Add,
            title = "New Collection",
            subtitle = "Group entries you want at hand",
            onClick = {},
            filled = false,
        )
    }
}

@Preview
@Composable
private fun PreviewActionCardLight() {
    AppThemePreview(darkTheme = false) { ActionCardPreview() }
}

@Preview
@Composable
private fun PreviewActionCardDark() {
    AppThemePreview(darkTheme = true) { ActionCardPreview() }
}
