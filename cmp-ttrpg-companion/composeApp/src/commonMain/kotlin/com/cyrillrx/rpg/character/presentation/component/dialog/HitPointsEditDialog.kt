package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.character.domain.HitPointAdjustment
import com.cyrillrx.rpg.character.domain.HitPoints
import com.cyrillrx.rpg.character.presentation.HitPointsEditorState
import com.cyrillrx.rpg.character.presentation.PreviewOutcome
import com.cyrillrx.rpg.character.presentation.cleared
import com.cyrillrx.rpg.character.presentation.withAdjustment
import com.cyrillrx.rpg.character.presentation.withAmountAdded
import com.cyrillrx.rpg.character.presentation.withDigitAppended
import com.cyrillrx.rpg.character.presentation.withLastDigitRemoved
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.dialog.EditDialog
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.LocalHealthColors
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.badge_dead
import rpg_companion.composeapp.generated.resources.badge_down
import rpg_companion.composeapp.generated.resources.hp_subtitle_damage
import rpg_companion.composeapp.generated.resources.hp_subtitle_healing
import rpg_companion.composeapp.generated.resources.hp_subtitle_maximum
import rpg_companion.composeapp.generated.resources.hp_subtitle_temp
import rpg_companion.composeapp.generated.resources.hp_tab_damage
import rpg_companion.composeapp.generated.resources.hp_tab_healing
import rpg_companion.composeapp.generated.resources.hp_title_damage
import rpg_companion.composeapp.generated.resources.hp_title_healing
import rpg_companion.composeapp.generated.resources.hp_title_maximum
import rpg_companion.composeapp.generated.resources.hp_title_temp
import rpg_companion.composeapp.generated.resources.label_hp_result
import rpg_companion.composeapp.generated.resources.label_max_hp
import rpg_companion.composeapp.generated.resources.label_temp_hp
import rpg_companion.composeapp.generated.resources.value_hp_transition

private val shortcuts = listOf(1, 2, 5, 10, 25, 50)

@Composable
internal fun HitPointsEditDialog(
    hitPoints: HitPoints,
    initialAdjustment: HitPointAdjustment,
    onConfirm: (HitPointAdjustment, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var editor by remember(hitPoints, initialAdjustment) {
        mutableStateOf(HitPointsEditorState(hitPoints = hitPoints, adjustment = initialAdjustment))
    }

    EditDialog(
        title = stringResource(editor.adjustment.title),
        subtitle = stringResource(editor.adjustment.subtitle),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(editor.adjustment, editor.amount) },
        confirmEnabled = editor.isApplyEnabled,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingCommon),
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            AdjustmentSelector(
                selected = editor.adjustment,
                onSelected = { editor = editor.withAdjustment(it) },
            )
            ResultCard(editor)
            NumberKeypad(
                onDigit = { editor = editor.withDigitAppended(it) },
                onClear = { editor = editor.cleared() },
                onBackspace = { editor = editor.withLastDigitRemoved() },
            )
            if (editor.showsShortcuts) {
                ShortcutRow(onShortcutTapped = { editor = editor.withAmountAdded(it) })
            }
        }
    }
}

@Composable
private fun AdjustmentSelector(
    selected: HitPointAdjustment,
    onSelected: (HitPointAdjustment) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        HitPointAdjustment.entries.forEachIndexed { index, adjustment ->
            SegmentedButton(
                selected = adjustment == selected,
                onClick = { onSelected(adjustment) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = HitPointAdjustment.entries.size),
                icon = {},
                label = {
                    Text(
                        text = stringResource(adjustment.tabLabel),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        }
    }
}

@Composable
private fun ResultCard(editor: HitPointsEditorState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = editor.adjustment.containerColor),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingCommon),
            modifier = Modifier.padding(spacingCommon),
        ) {
            Text(
                text = editor.signedAmount,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = editor.adjustment.accentColor,
            )
            Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
                Text(
                    text = stringResource(Res.string.label_hp_result),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                ) {
                    Text(
                        text = stringResource(
                            Res.string.value_hp_transition,
                            editor.currentRatio,
                            editor.previewRatio,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    when (editor.outcome) {
                        PreviewOutcome.LETHAL -> OutcomeBadge(Res.string.badge_dead)
                        PreviewOutcome.DOWN -> OutcomeBadge(Res.string.badge_down)
                        PreviewOutcome.STANDING, PreviewOutcome.NONE -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun OutcomeBadge(label: StringResource) {
    Surface(
        color = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingSmall),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShortcutRow(onShortcutTapped: (Int) -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier.fillMaxWidth(),
    ) {
        shortcuts.forEach { shortcut ->
            AssistChip(
                onClick = { onShortcutTapped(shortcut) },
                label = { Text(shortcut.toSignedString()) },
            )
        }
    }
}

private val HitPointAdjustment.title: StringResource
    get() = when (this) {
        HitPointAdjustment.DAMAGE -> Res.string.hp_title_damage
        HitPointAdjustment.HEALING -> Res.string.hp_title_healing
        HitPointAdjustment.TEMPORARY -> Res.string.hp_title_temp
        HitPointAdjustment.MAXIMUM -> Res.string.hp_title_maximum
    }

private val HitPointAdjustment.subtitle: StringResource
    get() = when (this) {
        HitPointAdjustment.DAMAGE -> Res.string.hp_subtitle_damage
        HitPointAdjustment.HEALING -> Res.string.hp_subtitle_healing
        HitPointAdjustment.TEMPORARY -> Res.string.hp_subtitle_temp
        HitPointAdjustment.MAXIMUM -> Res.string.hp_subtitle_maximum
    }

private val HitPointAdjustment.tabLabel: StringResource
    get() = when (this) {
        HitPointAdjustment.DAMAGE -> Res.string.hp_tab_damage
        HitPointAdjustment.HEALING -> Res.string.hp_tab_healing
        HitPointAdjustment.TEMPORARY -> Res.string.label_temp_hp
        HitPointAdjustment.MAXIMUM -> Res.string.label_max_hp
    }

private val HitPointAdjustment.accentColor: Color
    @Composable get() = when (this) {
        HitPointAdjustment.DAMAGE -> MaterialTheme.colorScheme.error
        HitPointAdjustment.HEALING -> LocalHealthColors.current.heal
        HitPointAdjustment.TEMPORARY -> MaterialTheme.colorScheme.tertiary
        HitPointAdjustment.MAXIMUM -> MaterialTheme.colorScheme.secondary
    }

private val HitPointAdjustment.containerColor: Color
    @Composable get() = when (this) {
        HitPointAdjustment.DAMAGE -> MaterialTheme.colorScheme.errorContainer
        HitPointAdjustment.HEALING -> LocalHealthColors.current.healContainer
        HitPointAdjustment.TEMPORARY -> MaterialTheme.colorScheme.tertiaryContainer
        HitPointAdjustment.MAXIMUM -> MaterialTheme.colorScheme.secondaryContainer
    }

@Preview
@Composable
private fun PreviewHitPointsEditDialogLight() {
    AppThemePreview(darkTheme = false) { HitPointsEditDialogPreview() }
}

@Preview
@Composable
private fun PreviewHitPointsEditDialogDark() {
    AppThemePreview(darkTheme = true) { HitPointsEditDialogPreview() }
}

@Composable
private fun HitPointsEditDialogPreview() {
    HitPointsEditDialog(
        hitPoints = HitPoints(current = 22, max = 24, temporary = 5),
        initialAdjustment = HitPointAdjustment.DAMAGE,
        onConfirm = { _, _ -> },
        onDismiss = {},
    )
}
