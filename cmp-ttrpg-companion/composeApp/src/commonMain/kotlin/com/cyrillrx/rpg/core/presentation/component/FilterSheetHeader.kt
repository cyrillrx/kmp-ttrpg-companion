package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_reset_all

/**
 * Header row for a filter bottom sheet: a title on the left and a "reset all" button on the right.
 */
@Composable
fun FilterSheetHeader(
    title: String,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        TextButton(onClick = onResetFilters) {
            Text(text = stringResource(Res.string.btn_reset_all))
        }
    }
}

@Preview
@Composable
private fun PreviewFilterSheetHeaderLight() {
    AppThemePreview(darkTheme = false) {
        FilterSheetHeader(title = "Filters", onResetFilters = {})
    }
}

@Preview
@Composable
private fun PreviewFilterSheetHeaderDark() {
    AppThemePreview(darkTheme = true) {
        FilterSheetHeader(title = "Filters", onResetFilters = {})
    }
}
