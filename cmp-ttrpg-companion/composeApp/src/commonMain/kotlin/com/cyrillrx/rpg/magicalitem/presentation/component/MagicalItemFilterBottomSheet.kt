package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingLarge
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemFilter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_reset_all
import rpg_companion.composeapp.generated.resources.label_filter_rarity
import rpg_companion.composeapp.generated.resources.label_filter_type
import rpg_companion.composeapp.generated.resources.title_filter_items

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MagicalItemFilterBottomSheet(
    filter: MagicalItemFilter,
    onTypeToggled: (MagicalItem.Type) -> Unit,
    onRarityToggled: (MagicalItem.Rarity) -> Unit,
    onResetFilters: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacingCommon)
                .padding(bottom = spacingLarge),
            verticalArrangement = Arrangement.spacedBy(spacingCommon),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.title_filter_items),
                    style = MaterialTheme.typography.titleLarge,
                )
                TextButton(onClick = onResetFilters) {
                    Text(text = stringResource(Res.string.btn_reset_all))
                }
            }

            // Type
            Text(
                text = stringResource(Res.string.label_filter_type),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            ) {
                MagicalItem.Type.entries.forEach { type ->
                    FilterChip(
                        selected = type in filter.types,
                        onClick = { onTypeToggled(type) },
                        label = { Text(text = type.toFormattedString()) },
                    )
                }
            }

            // Rarity
            Text(
                text = stringResource(Res.string.label_filter_rarity),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            ) {
                MagicalItem.Rarity.entries.forEach { rarity ->
                    FilterChip(
                        selected = rarity in filter.rarities,
                        onClick = { onRarityToggled(rarity) },
                        label = { Text(text = rarity.toFormattedString()) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMagicalItemFilterBottomSheetLight() {
    val sampleFilter = MagicalItemFilter(
        types = setOf(MagicalItem.Type.WEAPON),
        rarities = setOf(MagicalItem.Rarity.RARE),
    )
    AppThemePreview(darkTheme = false) {
        MagicalItemFilterBottomSheet(sampleFilter, {}, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewMagicalItemFilterBottomSheetDark() {
    val sampleFilter = MagicalItemFilter(
        types = setOf(MagicalItem.Type.WEAPON),
        rarities = setOf(MagicalItem.Rarity.RARE),
    )
    AppThemePreview(darkTheme = true) {
        MagicalItemFilterBottomSheet(sampleFilter, {}, {}, {}, {})
    }
}
