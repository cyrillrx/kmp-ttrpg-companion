package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.cyrillrx.rpg.core.presentation.component.dnd.SUBTITLE_SEPARATOR
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.iconSizeSmall
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Compendium list-item subtitle: optional leading [icon] and [type] tinted with [color], followed by
 * [subtitle] in `onSurfaceVariant`. Rendered as one [Text] so the line ellipsizes as a whole.
 */
@Composable
fun TintedSubtitle(
    icon: ImageVector?,
    type: String,
    color: Color,
    subtitle: String?,
    modifier: Modifier = Modifier,
) {
    val text = buildSubtitle(
        type = type,
        typeColor = color,
        subtitle = subtitle,
        subtitleColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (icon == null) {
        SubtitleText(text, modifier)
        return
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(iconSizeSmall),
        )
        Spacer(modifier = Modifier.width(spacingSmall))
        SubtitleText(text)
    }
}

internal fun buildSubtitle(
    type: String,
    typeColor: Color,
    subtitle: String?,
    subtitleColor: Color,
): AnnotatedString = buildAnnotatedString {
    withStyle(SpanStyle(color = typeColor)) { append(type) }
    subtitle?.let {
        withStyle(SpanStyle(color = subtitleColor)) { append(subtitle) }
    }
}

@Composable
private fun SubtitleText(text: AnnotatedString, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PreviewTintedSubtitleLight() {
    TintedSubtitlePreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewTintedSubtitleDark() {
    TintedSubtitlePreview(darkTheme = true)
}

@Composable
private fun TintedSubtitlePreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        val accent = MaterialTheme.colorScheme.primary
        val subtitle = SUBTITLE_SEPARATOR + "Large" + SUBTITLE_SEPARATOR + "Lawful Evil"
        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            TintedSubtitle(icon = Icons.Filled.Pets, type = "Dragon", color = accent, subtitle = subtitle)
            TintedSubtitle(icon = null, type = "Weapon", color = accent, subtitle = subtitle)
        }
    }
}
