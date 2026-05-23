package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.clickable
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
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.domain.coerceToValidWalkSpeedInFeet
import com.cyrillrx.rpg.character.domain.coerceToValidWalkSpeedInMeters
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.core.presentation.LocalDistanceUnit
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.dnd.domain.feetToMeters
import com.cyrillrx.rpg.dnd.domain.metersToFeet
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.background_none
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
import rpg_companion.composeapp.generated.resources.label_race
import rpg_companion.composeapp.generated.resources.label_str
import rpg_companion.composeapp.generated.resources.label_walk_speed
import rpg_companion.composeapp.generated.resources.label_wis
import rpg_companion.composeapp.generated.resources.settings_unit_feet_abbr
import rpg_companion.composeapp.generated.resources.settings_unit_meters_abbr

@Composable
internal fun CharacterEditDialog(
    state: CharacterEditState.Loaded,
    onRaceConfirmed: (Race) -> Unit,
    onClassConfirmed: (Character.Class) -> Unit,
    onLevelConfirmed: (Int) -> Unit,
    onBackgroundConfirmed: (Background?) -> Unit,
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
        EditingField.Background -> SingleChoiceDialog(
            title = stringResource(Res.string.label_background),
            selected = state.character.background,
            options = Background.entries,
            optionLabel = { it.toFormattedString() },
            noneLabel = stringResource(Res.string.background_none),
            onConfirm = onBackgroundConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Level -> NumberEditDialog(
            title = stringResource(Res.string.label_level),
            initialValue = state.character.level,
            onConfirm = onLevelConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Strength -> NumberEditDialog(
            title = stringResource(Res.string.label_str),
            initialValue = state.character.abilities.strength.value,
            onConfirm = onStrengthConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Dexterity -> NumberEditDialog(
            title = stringResource(Res.string.label_dex),
            initialValue = state.character.abilities.dexterity.value,
            onConfirm = onDexterityConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Constitution -> NumberEditDialog(
            title = stringResource(Res.string.label_con),
            initialValue = state.character.abilities.constitution.value,
            onConfirm = onConstitutionConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Intelligence -> NumberEditDialog(
            title = stringResource(Res.string.label_int),
            initialValue = state.character.abilities.intelligence.value,
            onConfirm = onIntelligenceConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Wisdom -> NumberEditDialog(
            title = stringResource(Res.string.label_wis),
            initialValue = state.character.abilities.wisdom.value,
            onConfirm = onWisdomConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Charisma -> NumberEditDialog(
            title = stringResource(Res.string.label_cha),
            initialValue = state.character.abilities.charisma.value,
            onConfirm = onCharismaConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.ArmorClass -> NumberEditDialog(
            title = stringResource(Res.string.label_ac),
            initialValue = state.character.armorClass,
            onConfirm = onArmorClassConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.MaxHitPoints -> NumberEditDialog(
            title = stringResource(Res.string.label_max_hp),
            initialValue = state.character.maxHitPoints,
            onConfirm = onMaxHitPointsConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.WalkSpeed -> {
            val unit = LocalDistanceUnit.current
            val walkFeet = state.character.speeds.walk
            val unitAbbrRes = when (unit) {
                DistanceUnit.FEET -> Res.string.settings_unit_feet_abbr
                DistanceUnit.METERS -> Res.string.settings_unit_meters_abbr
            }
            val title = stringResource(Res.string.label_walk_speed, stringResource(unitAbbrRes))
            when (unit) {
                DistanceUnit.FEET -> NumberEditDialog(
                    title = title,
                    initialValue = walkFeet,
                    onConfirm = { entered -> onWalkSpeedConfirmed(entered.coerceToValidWalkSpeedInFeet()) },
                    onDismiss = onDismiss,
                )
                DistanceUnit.METERS -> FloatEditDialog(
                    title = title,
                    initialValue = walkFeet.feetToMeters(),
                    onConfirm = { entered -> onWalkSpeedConfirmed(entered.coerceToValidWalkSpeedInMeters().metersToFeet()) },
                    onDismiss = onDismiss,
                )
            }
        }

        EditingField.Race -> SingleChoiceDialog(
            title = stringResource(Res.string.label_race),
            selected = state.character.race,
            options = Race.entries,
            optionLabel = { it.toFormattedString() },
            onConfirm = { it?.let(onRaceConfirmed) },
            onDismiss = onDismiss,
        )

        EditingField.Clazz -> SingleChoiceDialog(
            title = stringResource(Res.string.label_class),
            selected = state.character.clazz,
            options = Character.Class.entries,
            optionLabel = { it.toFormattedString() },
            onConfirm = { it?.let(onClassConfirmed) },
            onDismiss = onDismiss,
        )

        EditingField.Languages -> LanguageSelectDialog(
            current = state.character.languages,
            onConfirm = onLanguagesConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Alignment -> SingleChoiceDialog(
            title = stringResource(Res.string.label_alignment),
            selected = state.character.alignment,
            options = Creature.Alignment.entries,
            optionLabel = { it.toFormattedString() },
            onConfirm = { it?.let(onAlignmentConfirmed) },
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
    isValid: (String) -> Boolean = { true },
) {
    var text by remember(initialValue) { mutableStateOf(initialValue) }
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
            val inputIsValid = remember(text) { isValid(text) }
            TextButton(
                onClick = { onConfirm(text) },
                enabled = inputIsValid,
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
private fun NumberEditDialog(
    title: String,
    initialValue: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember(initialValue) { mutableStateOf(initialValue.toString()) }
    val parsedValue = text.trim().toIntOrNull()
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
            TextButton(
                onClick = { parsedValue?.let(onConfirm) },
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

@Composable
private fun FloatEditDialog(
    title: String,
    initialValue: Float,
    onConfirm: (Float) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialText = if (initialValue % 1 == 0f) initialValue.toInt().toString() else initialValue.toString()
    var text by remember(initialValue) { mutableStateOf(initialText) }
    val parsedValue = text.trim().toFloatOrNull()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        confirmButton = {
            TextButton(
                onClick = { parsedValue?.let(onConfirm) },
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

@Composable
private fun <T : Any> SingleChoiceDialog(
    title: String,
    selected: T?,
    options: List<T>,
    optionLabel: @Composable (T) -> String,
    noneLabel: String? = null,
    onConfirm: (T?) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (noneLabel != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable { onConfirm(null) },
                    ) {
                        RadioButton(selected = selected == null, onClick = null)
                        Text(text = noneLabel, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable { onConfirm(option) },
                    ) {
                        RadioButton(selected = selected == option, onClick = null)
                        Text(
                            text = optionLabel(option),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {},
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
                        modifier = Modifier.fillMaxWidth().clickable {
                            selected = if (language in selected) selected - language else selected + language
                        },
                    ) {
                        Checkbox(
                            checked = language in selected,
                            onCheckedChange = null,
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
