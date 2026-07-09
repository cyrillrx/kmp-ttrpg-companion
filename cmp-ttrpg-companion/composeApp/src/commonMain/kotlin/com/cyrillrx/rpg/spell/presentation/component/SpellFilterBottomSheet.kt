package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingLarge
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spell.domain.ComponentFilter
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellFilter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_reset_all
import rpg_companion.composeapp.generated.resources.label_filter_class
import rpg_companion.composeapp.generated.resources.label_filter_components
import rpg_companion.composeapp.generated.resources.label_filter_level
import rpg_companion.composeapp.generated.resources.label_filter_school
import rpg_companion.composeapp.generated.resources.title_filter_spells

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SpellFilterBottomSheet(
    filter: SpellFilter,
    onLevelToggled: (Int) -> Unit,
    onSchoolToggled: (Spell.School) -> Unit,
    onClassToggled: (Character.Class) -> Unit,
    onComponentToggled: (Spell.Component) -> Unit,
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
                    text = stringResource(Res.string.title_filter_spells),
                    style = MaterialTheme.typography.titleLarge,
                )
                TextButton(onClick = onResetFilters) {
                    Text(text = stringResource(Res.string.btn_reset_all))
                }
            }

            // Level
            Text(
                text = stringResource(Res.string.label_filter_level),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            ) {
                (0..9).forEach { level ->
                    FilterChip(
                        selected = level in filter.levels,
                        onClick = { onLevelToggled(level) },
                        label = { Text(text = level.toString()) },
                    )
                }
            }

            // Class
            Text(
                text = stringResource(Res.string.label_filter_class),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            ) {
                Character.Class.entries.filter { it != Character.Class.UNKNOWN }.forEach { characterClass ->
                    FilterChip(
                        selected = characterClass in filter.characterClasses,
                        onClick = { onClassToggled(characterClass) },
                        label = { Text(text = characterClass.toFormattedString()) },
                    )
                }
            }

            // School
            Text(
                text = stringResource(Res.string.label_filter_school),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            ) {
                Spell.School.entries.forEach { school ->
                    FilterChip(
                        selected = school in filter.schools,
                        onClick = { onSchoolToggled(school) },
                        label = { Text(text = school.toFormattedString()) },
                    )
                }
            }

            // Components (tri-state: indifferent, required, excluded)
            Text(
                text = stringResource(Res.string.label_filter_components),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            ) {
                Spell.Component.entries.forEach { component ->
                    ComponentFilterChip(
                        label = component.toFormattedString(),
                        state = filter.components[component],
                        onClick = { onComponentToggled(component) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComponentFilterChip(
    label: String,
    state: ComponentFilter?,
    onClick: () -> Unit,
) {
    val leadingIcon: ImageVector? = when (state) {
        ComponentFilter.REQUIRED -> Icons.Default.Check
        ComponentFilter.EXCLUDED -> Icons.Default.Close
        null -> null
    }
    val colors = when (state) {
        ComponentFilter.EXCLUDED -> FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onErrorContainer,
        )
        else -> FilterChipDefaults.filterChipColors()
    }
    FilterChip(
        selected = state != null,
        onClick = onClick,
        label = { Text(text = label) },
        leadingIcon = leadingIcon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                )
            }
        },
        colors = colors,
    )
}

@Preview
@Composable
private fun PreviewSpellFilterBottomSheetLight() {
    val sampleFilter = SpellFilter(
        levels = setOf(0, 3),
        characterClasses = setOf(Character.Class.SORCERER),
        schools = setOf(Spell.School.ABJURATION, Spell.School.EVOCATION),
        components = mapOf(
            Spell.Component.SOMATIC to ComponentFilter.REQUIRED,
            Spell.Component.MATERIAL to ComponentFilter.EXCLUDED,
        ),
    )
    AppThemePreview(darkTheme = false) {
        SpellFilterBottomSheet(sampleFilter, {}, {}, {}, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewSpellFilterBottomSheetDark() {
    val sampleFilter = SpellFilter(
        levels = setOf(0, 3),
        characterClasses = setOf(Character.Class.SORCERER),
        schools = setOf(Spell.School.ABJURATION, Spell.School.EVOCATION),
        components = mapOf(
            Spell.Component.SOMATIC to ComponentFilter.REQUIRED,
            Spell.Component.MATERIAL to ComponentFilter.EXCLUDED,
        ),
    )
    AppThemePreview(darkTheme = true) {
        SpellFilterBottomSheet(sampleFilter, {}, {}, {}, {}, {}, {})
    }
}
