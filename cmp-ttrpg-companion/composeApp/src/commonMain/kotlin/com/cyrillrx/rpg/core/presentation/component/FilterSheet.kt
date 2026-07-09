package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_reset_all

/**
 * Header row for a filter bottom sheet: a title on the left and a "reset all" button on the right.
 */
@Composable
fun FilterSheetHeader(
    title: String,
    onResetFilters: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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

/**
 * A titled section of a filter bottom sheet: a small title followed by a [FlowRow] of chips.
 * Vertical spacing between sections is expected to be provided by the parent column.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSection(
    title: String,
    content: @Composable FlowRowScope.() -> Unit,
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
