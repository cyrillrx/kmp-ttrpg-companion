package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.Proficiency
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_cancel
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.label_saving_throw_proficiency

@Composable
internal fun AbilityEditDialog(
    title: String,
    initialValue: Int,
    initialProficiency: Proficiency,
    onConfirm: (Int, Proficiency) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember(initialValue) { mutableStateOf(initialValue.toString()) }
    var proficiency by remember(initialProficiency) { mutableStateOf(initialProficiency) }
    val parsedValue = text.trim().toIntOrNull()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            proficiency = if (proficiency == Proficiency.NONE) Proficiency.PROFICIENT else Proficiency.NONE
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
                onClick = { parsedValue?.let { onConfirm(it, proficiency) } },
                enabled = parsedValue != null,
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
