package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalIconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cyrillrx.rpg.character.domain.MAX_ABILITY_SCORE
import com.cyrillrx.rpg.character.domain.MIN_ABILITY_SCORE
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Proficiency
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_cancel
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.hint_ability
import rpg_companion.composeapp.generated.resources.label_saving_throw_proficiency

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
                        modifier = Modifier.size(56.dp),
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
                        modifier = Modifier.size(56.dp),
                    ) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            proficiency =
                                if (proficiency == Proficiency.NONE) Proficiency.PROFICIENT else Proficiency.NONE
                        }
                        .padding(vertical = spacingMedium),
                ) {
                    Checkbox(checked = proficiency != Proficiency.NONE, onCheckedChange = null)
                    Text(
                        text = stringResource(Res.string.label_saving_throw_proficiency),
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
