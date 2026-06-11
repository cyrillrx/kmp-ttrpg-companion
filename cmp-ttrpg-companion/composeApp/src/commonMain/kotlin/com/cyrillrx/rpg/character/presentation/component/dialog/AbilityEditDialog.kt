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
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Proficiency
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_cancel
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.hint_ability_initial_stats
import rpg_companion.composeapp.generated.resources.hint_ability_range
import rpg_companion.composeapp.generated.resources.label_saving_throw_proficiency

private const val MIN_ABILITY_SCORE = 1
private const val MAX_ABILITY_SCORE = 30

@Composable
internal fun AbilityEditDialog(
    title: String,
    initialValue: Int,
    initialProficiency: Proficiency,
    onConfirm: (Int, Proficiency) -> Unit,
    onDismiss: () -> Unit,
) {
    var value by remember(initialValue) {
        mutableIntStateOf(
            initialValue.coerceIn(
                MIN_ABILITY_SCORE,
                MAX_ABILITY_SCORE,
            ),
        )
    }
    var proficiency by remember(initialProficiency) { mutableStateOf(initialProficiency) }
    val initialModifier = remember(initialValue) { Ability(initialValue).getModifier() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(title)
                Text(
                    text = stringResource(
                        Res.string.hint_ability_initial_stats,
                        initialValue,
                        initialModifier.toSignedString(),
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
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                    )
                    FilledTonalIconButton(
                        onClick = { value++ },
                        enabled = value < MAX_ABILITY_SCORE,
                        modifier = Modifier.size(56.dp),
                    ) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                Text(
                    text = stringResource(Res.string.hint_ability_range, MIN_ABILITY_SCORE, MAX_ABILITY_SCORE),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
            TextButton(onClick = { onConfirm(value, proficiency) }) {
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
