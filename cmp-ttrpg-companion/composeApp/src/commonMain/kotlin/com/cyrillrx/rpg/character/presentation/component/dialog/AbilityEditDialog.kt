package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.cyrillrx.rpg.character.domain.MAX_ABILITY_SCORE
import com.cyrillrx.rpg.character.domain.MIN_ABILITY_SCORE
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.theme.iconButtonSize
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Proficiency
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_cancel
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.hint_ability
import rpg_companion.composeapp.generated.resources.label_saving_throw_proficiency
import rpg_companion.composeapp.generated.resources.proficiency_expert
import rpg_companion.composeapp.generated.resources.proficiency_none
import rpg_companion.composeapp.generated.resources.proficiency_proficient

@Composable
internal fun AbilityEditDialog(
    title: String,
    initialAbility: Ability,
    onConfirm: (Ability) -> Unit,
    onDismiss: () -> Unit,
) {
    var value by remember(initialAbility) {
        mutableIntStateOf(initialAbility.value.coerceIn(MIN_ABILITY_SCORE, MAX_ABILITY_SCORE))
    }
    var proficiency by remember(initialAbility) { mutableStateOf(initialAbility.savingThrowProficiency) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(title)
                Text(
                    text = stringResource(
                        Res.string.hint_ability,
                        MIN_ABILITY_SCORE,
                        MAX_ABILITY_SCORE,
                        initialAbility.getValueWithModifier(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    FilledTonalIconButton(
                        onClick = { value-- },
                        enabled = value > MIN_ABILITY_SCORE,
                        modifier = Modifier.size(iconButtonSize),
                    ) {
                        Text("-", style = MaterialTheme.typography.headlineMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = Ability(value).getModifier().toSignedString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    FilledTonalIconButton(
                        onClick = { value++ },
                        enabled = value < MAX_ABILITY_SCORE,
                        modifier = Modifier.size(iconButtonSize),
                    ) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                val (icon, tint) = when (proficiency) {
                    Proficiency.NONE -> Icons.Outlined.CheckBoxOutlineBlank to MaterialTheme.colorScheme.onSurfaceVariant
                    Proficiency.PROFICIENT -> Icons.Filled.CheckBox to MaterialTheme.colorScheme.primary
                    Proficiency.EXPERT -> Icons.Filled.Star to MaterialTheme.colorScheme.tertiary
                }
                val stateLabel = stringResource(
                    when (proficiency) {
                        Proficiency.NONE -> Res.string.proficiency_none
                        Proficiency.PROFICIENT -> Res.string.proficiency_proficient
                        Proficiency.EXPERT -> Res.string.proficiency_expert
                    },
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { proficiency = proficiency.next() }
                        .padding(vertical = spacingMedium),
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = tint)
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(Res.string.label_saving_throw_proficiency))
                            append(": ")
                            withStyle(SpanStyle(color = tint)) { append(stateLabel) }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = spacingMedium),
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(initialAbility.copy(value = value, savingThrowProficiency = proficiency)) },
            ) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

private fun Proficiency.next(): Proficiency = when (this) {
    Proficiency.NONE -> Proficiency.PROFICIENT
    Proficiency.PROFICIENT -> Proficiency.EXPERT
    Proficiency.EXPERT -> Proficiency.NONE
}
