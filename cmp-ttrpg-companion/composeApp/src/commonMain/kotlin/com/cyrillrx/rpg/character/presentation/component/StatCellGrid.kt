package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium

/**
 * Lays out [cells] in a grid of [columns] equally-weighted columns, spaced by [spacingMedium].
 * Each cell receives the [Modifier] carrying its column weight.
 */
@Composable
internal fun StatCellGrid(
    columns: Int,
    cells: List<@Composable (Modifier) -> Unit>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingMedium)) {
        cells.chunked(columns).forEach { rowCells ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier = Modifier.fillMaxWidth(),
            ) {
                rowCells.forEach { cell -> cell(Modifier.weight(1f)) }
            }
        }
    }
}
