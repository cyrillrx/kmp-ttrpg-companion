package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A titled section of a filter bottom sheet: a small title above a [FlowRow] of filter controls.
 *
 * The [content] is emitted directly inside the [FlowRow], so callers have access to [FlowRowScope].
 * Vertical spacing between sections is expected to be provided by the parent column.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable FlowRowScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            content = content,
        )
    }
}

@Composable
private fun FilterSectionSample() {
    FilterSection(title = "Rarity") {
        FilterChip(selected = true, onClick = {}, label = { Text("Common") })
        FilterChip(selected = false, onClick = {}, label = { Text("Rare") })
        FilterChip(selected = true, onClick = {}, label = { Text("Legendary") })
    }
}

@Preview
@Composable
private fun PreviewFilterSectionLight() {
    AppThemePreview(darkTheme = false) {
        FilterSectionSample()
    }
}

@Preview
@Composable
private fun PreviewFilterSectionDark() {
    AppThemePreview(darkTheme = true) {
        FilterSectionSample()
    }
}
