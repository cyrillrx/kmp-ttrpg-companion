package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CompendiumButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(spacingMedium),
        ) {
            Icon(
                imageVector = icon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = label,
                modifier = Modifier
                    .size(32.dp)
                    .padding(bottom = spacingSmall),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CompendiumButtonPreview() {
    CompendiumButton(
        label = "Spellbook",
        icon = Icons.Filled.AutoAwesome,
        onClick = {},
        modifier = Modifier.width(100.dp),
    )
}

@Preview
@Composable
private fun PreviewCompendiumButtonLight() {
    AppThemePreview(darkTheme = false) {
        CompendiumButtonPreview()
    }
}

@Preview
@Composable
private fun PreviewCompendiumButtonDark() {
    AppThemePreview(darkTheme = true) {
        CompendiumButtonPreview()
    }
}
