package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.EditingField
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_cancel
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.label_ac
import rpg_companion.composeapp.generated.resources.label_alignment
import rpg_companion.composeapp.generated.resources.label_background
import rpg_companion.composeapp.generated.resources.label_cha
import rpg_companion.composeapp.generated.resources.label_class
import rpg_companion.composeapp.generated.resources.label_con
import rpg_companion.composeapp.generated.resources.label_dex
import rpg_companion.composeapp.generated.resources.label_int
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_level
import rpg_companion.composeapp.generated.resources.label_max_hp
import rpg_companion.composeapp.generated.resources.label_name
import rpg_companion.composeapp.generated.resources.label_race
import rpg_companion.composeapp.generated.resources.label_str
import rpg_companion.composeapp.generated.resources.label_walk_speed
import rpg_companion.composeapp.generated.resources.label_wis

@Composable
internal fun CharacterEditDialog(
    state: CharacterEditState,
    onNameConfirmed: (String) -> Unit,
    onRaceConfirmed: (Race) -> Unit,
    onClassConfirmed: (Character.Class) -> Unit,
    onLevelConfirmed: (Int) -> Unit,
    onBackgroundConfirmed: (String) -> Unit,
    onStrengthConfirmed: (Int) -> Unit,
    onDexterityConfirmed: (Int) -> Unit,
    onConstitutionConfirmed: (Int) -> Unit,
    onIntelligenceConfirmed: (Int) -> Unit,
    onWisdomConfirmed: (Int) -> Unit,
    onCharismaConfirmed: (Int) -> Unit,
    onArmorClassConfirmed: (Int) -> Unit,
    onMaxHitPointsConfirmed: (Int) -> Unit,
    onWalkSpeedConfirmed: (Int) -> Unit,
    onLanguagesConfirmed: (List<Language>) -> Unit,
    onAlignmentConfirmed: (Creature.Alignment) -> Unit,
    onDismiss: () -> Unit,
) {
    val field = state.editingField ?: return

    when (field) {
        EditingField.Name -> TextEditDialog(
            title = stringResource(Res.string.label_name),
            initialValue = state.name,
            onConfirm = onNameConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Background -> TextEditDialog(
            title = stringResource(Res.string.label_background),
            initialValue = state.background,
            onConfirm = onBackgroundConfirmed,
            onDismiss = onDismiss,
            singleLine = false,
        )

        EditingField.Level -> NumberEditDialog(
            title = stringResource(Res.string.label_level),
            initialValue = state.level,
            onConfirm = onLevelConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Strength -> NumberEditDialog(
            title = stringResource(Res.string.label_str),
            initialValue = state.strength,
            onConfirm = onStrengthConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Dexterity -> NumberEditDialog(
            title = stringResource(Res.string.label_dex),
            initialValue = state.dexterity,
            onConfirm = onDexterityConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Constitution -> NumberEditDialog(
            title = stringResource(Res.string.label_con),
            initialValue = state.constitution,
            onConfirm = onConstitutionConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Intelligence -> NumberEditDialog(
            title = stringResource(Res.string.label_int),
            initialValue = state.intelligence,
            onConfirm = onIntelligenceConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Wisdom -> NumberEditDialog(
            title = stringResource(Res.string.label_wis),
            initialValue = state.wisdom,
            onConfirm = onWisdomConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Charisma -> NumberEditDialog(
            title = stringResource(Res.string.label_cha),
            initialValue = state.charisma,
            onConfirm = onCharismaConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.ArmorClass -> NumberEditDialog(
            title = stringResource(Res.string.label_ac),
            initialValue = state.armorClass,
            onConfirm = onArmorClassConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.MaxHitPoints -> NumberEditDialog(
            title = stringResource(Res.string.label_max_hp),
            initialValue = state.maxHitPoints,
            onConfirm = onMaxHitPointsConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.WalkSpeed -> NumberEditDialog(
            title = stringResource(Res.string.label_walk_speed),
            initialValue = state.walkSpeed,
            onConfirm = onWalkSpeedConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Race -> RaceSelectDialog(
            current = state.race,
            onConfirm = onRaceConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Clazz -> ClassSelectDialog(
            current = state.clazz,
            onConfirm = onClassConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Languages -> LanguageSelectDialog(
            current = state.languages,
            onConfirm = onLanguagesConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Alignment -> AlignmentSelectDialog(
            current = state.alignment,
            onConfirm = onAlignmentConfirmed,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun TextEditDialog(
    title: String,
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    singleLine: Boolean = true,
) {
    var text by remember(title) { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = singleLine,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) {
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

@Composable
private fun NumberEditDialog(
    title: String,
    initialValue: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember(title) { mutableStateOf(initialValue.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
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
        },
        confirmButton = {
            val isInputValid = remember(text) { text.toIntOrNull() != null }
            TextButton(
                onClick = { text.toIntOrNull()?.let(onConfirm) },
                enabled = isInputValid,
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


@Composable
private fun RaceSelectDialog(
    current: Race,
    onConfirm: (Race) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_race)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Race.entries.forEach { race ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        RadioButton(
                            selected = selected == race,
                            onClick = { selected = race },
                        )
                        Text(
                            text = race.toFormattedString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
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

@Composable
private fun ClassSelectDialog(
    current: Character.Class,
    onConfirm: (Character.Class) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_class)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Character.Class.entries.forEach { clazz ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        RadioButton(
                            selected = selected == clazz,
                            onClick = { selected = clazz },
                        )
                        Text(
                            text = clazz.toFormattedString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
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

@Composable
private fun AlignmentSelectDialog(
    current: Creature.Alignment,
    onConfirm: (Creature.Alignment) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_alignment)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Creature.Alignment.entries.forEach { alignment ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        RadioButton(
                            selected = selected == alignment,
                            onClick = { selected = alignment },
                        )
                        Text(
                            text = alignment.toFormattedString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
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

@Composable
private fun LanguageSelectDialog(
    current: List<Language>,
    onConfirm: (List<Language>) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current.toSet()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_languages)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Language.entries.forEach { language ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Checkbox(
                            checked = language in selected,
                            onCheckedChange = { checked ->
                                selected = if (checked) selected + language else selected - language
                            },
                        )
                        Text(
                            text = language.toFormattedString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected.toList()) }) {
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
