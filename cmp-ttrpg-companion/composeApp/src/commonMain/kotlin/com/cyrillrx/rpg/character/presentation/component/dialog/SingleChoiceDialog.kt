package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium

@Composable
internal fun <T : Any> SingleChoiceDialog(
    title: String,
    selected: T?,
    options: List<T>,
    optionLabel: @Composable (T) -> String,
    noneLabel: String? = null,
    onConfirm: (T?) -> Unit,
    onDismiss: () -> Unit,
) {
    // Matches the AlertDialog horizontal content padding so rows can bleed edge to edge.
    val rowHorizontalPadding = spacingCommon + spacingMedium

    EditDialog(
        title = title,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .bleedHorizontally(rowHorizontalPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            if (noneLabel != null) {
                ChoiceRow(
                    label = noneLabel,
                    isSelected = selected == null,
                    horizontalPadding = rowHorizontalPadding,
                    onClick = { onConfirm(null) },
                )
            }
            options.forEach { option ->
                ChoiceRow(
                    label = optionLabel(option),
                    isSelected = selected == option,
                    horizontalPadding = rowHorizontalPadding,
                    onClick = { onConfirm(option) },
                )
            }
        }
    }
}

@Composable
private fun ChoiceRow(
    label: String,
    isSelected: Boolean,
    horizontalPadding: Dp,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { selected = isSelected }
            .clickable(onClick = onClick)
            .padding(horizontal = horizontalPadding, vertical = spacingMedium),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/**
 * Expands the content horizontally by [amount] on each side so it can bleed past the
 * AlertDialog's content padding, while still reporting its original width to the parent.
 * This lets the row clickable ripple span the full dialog width without disturbing the
 * surrounding AlertDialog layout.
 */
private fun Modifier.bleedHorizontally(amount: Dp): Modifier = layout { measurable, constraints ->
    val grow = (amount * 2).roundToPx()
    val expanded = if (constraints.hasBoundedWidth) {
        constraints.copy(maxWidth = constraints.maxWidth + grow)
    } else {
        constraints
    }
    val placeable = measurable.measure(expanded)
    val reportedWidth = (placeable.width - grow).coerceAtLeast(0)
    layout(reportedWidth, placeable.height) {
        placeable.place(-amount.roundToPx(), 0)
    }
}
