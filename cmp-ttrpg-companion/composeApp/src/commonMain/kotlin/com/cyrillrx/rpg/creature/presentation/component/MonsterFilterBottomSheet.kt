package com.cyrillrx.rpg.creature.presentation.component

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
import com.cyrillrx.rpg.core.presentation.component.dnd.formatCRValue
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingLarge
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_reset_all
import rpg_companion.composeapp.generated.resources.label_filter_cr
import rpg_companion.composeapp.generated.resources.label_filter_type
import rpg_companion.composeapp.generated.resources.title_filter_creatures

private val commonCRs = listOf(
    0f, 0.125f, 0.25f, 0.5f,
    1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f,
    11f, 12f, 13f, 14f, 15f, 16f, 17f,
    20f, 21f, 24f, 30f,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MonsterFilterBottomSheet(
    filter: MonsterFilter,
    onTypeToggled: (Monster.Type) -> Unit,
    onChallengeRatingToggled: (Float) -> Unit,
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
                    text = stringResource(Res.string.title_filter_creatures),
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
                Monster.Type.entries.forEach { type ->
                    FilterChip(
                        selected = type in filter.types,
                        onClick = { onTypeToggled(type) },
                        label = { Text(text = type.toFormattedString()) },
                    )
                }
            }

            // Challenge Rating
            Text(
                text = stringResource(Res.string.label_filter_cr),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            ) {
                commonCRs.forEach { cr ->
                    val crLabel = formatCRValue(cr)
                    FilterChip(
                        selected = cr in filter.challengeRatings,
                        onClick = { onChallengeRatingToggled(cr) },
                        label = { Text(text = crLabel) },
                    )
                }
            }
        }
    }
}

private val sampleFilter = MonsterFilter(
    types = setOf(Monster.Type.DRAGON),
    challengeRatings = setOf(10f),
)

@Preview
@Composable
private fun PreviewMonsterFilterBottomSheetLight() {
    AppThemePreview(darkTheme = false) {
        MonsterFilterBottomSheet(sampleFilter, {}, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewMonsterFilterBottomSheetDark() {
    AppThemePreview(darkTheme = true) {
        MonsterFilterBottomSheet(sampleFilter, {}, {}, {}, {})
    }
}
