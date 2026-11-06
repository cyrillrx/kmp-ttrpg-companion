package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Small uppercase pill in a [tint] color over a faint [tint]-tinted background.
 * Used on detail screens to surface flags such as attunement, ritual or concentration.
 */
@Composable
fun TintedTag(text: String, tint: Color, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = tint,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(tint.copy(alpha = 0.15f))
            .padding(horizontal = spacingMedium, vertical = spacingSmall),
    )
}

@Preview
@Composable
private fun PreviewTintedTagLight() {
    AppThemePreview(darkTheme = false) {
        TintedTag(text = "Requires attunement", tint = MaterialTheme.colorScheme.primary)
    }
}

@Preview
@Composable
private fun PreviewTintedTagDark() {
    AppThemePreview(darkTheme = true) {
        TintedTag(text = "Requires attunement", tint = MaterialTheme.colorScheme.primary)
    }
}
