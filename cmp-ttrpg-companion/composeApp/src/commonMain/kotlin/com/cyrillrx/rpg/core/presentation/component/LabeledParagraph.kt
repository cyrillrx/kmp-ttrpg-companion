package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A single stat-block line: a bold [label] followed by ". " and the plain [value].
 * Renders nothing when [value] is blank. Shared by the spell material line and the
 * monster stat block.
 */
@Composable
fun LabeledParagraph(label: String, value: String, modifier: Modifier = Modifier) {
    if (value.isBlank()) return
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(label)
                append(". ")
            }
            append(value)
        },
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PreviewLabeledParagraphLight() {
    AppThemePreview(darkTheme = false) {
        LabeledParagraph(label = "Senses", value = "darkvision 60 ft., passive Perception 13")
    }
}

@Preview
@Composable
private fun PreviewLabeledParagraphDark() {
    AppThemePreview(darkTheme = true) {
        LabeledParagraph(label = "Senses", value = "darkvision 60 ft., passive Perception 13")
    }
}
