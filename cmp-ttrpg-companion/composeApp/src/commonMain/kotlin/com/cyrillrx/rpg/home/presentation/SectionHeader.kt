package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(spacingCommon),
    )
}

@Composable
fun SectionHeaderWithAction(
    title: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = spacingCommon),
    ) {
        SectionHeader(title = title, modifier = Modifier)
        TextButton(onClick = onActionClick) {
            Text(text = actionLabel)
        }
    }
}

@Preview
@Composable
private fun PreviewSectionHeaderLight() {
    AppThemePreview(darkTheme = false) {
        SectionHeader(title = "Compendium")
    }
}

@Preview
@Composable
private fun PreviewSectionHeaderDark() {
    AppThemePreview(darkTheme = true) {
        SectionHeader(title = "Compendium")
    }
}

@Preview
@Composable
private fun PreviewSectionHeaderWithActionLight() {
    AppThemePreview(darkTheme = false) {
        SectionHeaderWithAction(
            title = "Compendium",
            actionLabel = "See all",
            onActionClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSectionHeaderWithActionDark() {
    AppThemePreview(darkTheme = true) {
        SectionHeaderWithAction(
            title = "Compendium",
            actionLabel = "See all",
            onActionClick = {},
        )
    }
}
