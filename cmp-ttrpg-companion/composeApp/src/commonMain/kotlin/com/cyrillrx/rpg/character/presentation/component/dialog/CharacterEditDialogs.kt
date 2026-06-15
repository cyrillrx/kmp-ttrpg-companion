package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.MAX_ARMOR_CLASS
import com.cyrillrx.rpg.character.domain.MAX_CHARACTER_LEVEL
import com.cyrillrx.rpg.character.domain.MIN_ARMOR_CLASS
import com.cyrillrx.rpg.character.domain.MIN_CHARACTER_LEVEL
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.LocalDistanceUnit
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.getProficiency
import com.cyrillrx.rpg.creature.domain.getRelatedAbilityScore
import com.cyrillrx.rpg.creature.domain.withProficiency
import com.cyrillrx.rpg.dnd.domain.feetToMeters
import com.cyrillrx.rpg.dnd.domain.metersToFeet
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.ability_label_charisma
import rpg_companion.composeapp.generated.resources.ability_label_constitution
import rpg_companion.composeapp.generated.resources.ability_label_dexterity
import rpg_companion.composeapp.generated.resources.ability_label_intelligence
import rpg_companion.composeapp.generated.resources.ability_label_strength
import rpg_companion.composeapp.generated.resources.ability_label_wisdom
import rpg_companion.composeapp.generated.resources.background_none
import rpg_companion.composeapp.generated.resources.hint_leave_blank_to_remove
import rpg_companion.composeapp.generated.resources.hint_short_description
import rpg_companion.composeapp.generated.resources.label_alignment
import rpg_companion.composeapp.generated.resources.label_armor_class
import rpg_companion.composeapp.generated.resources.label_background
import rpg_companion.composeapp.generated.resources.label_class
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_level
import rpg_companion.composeapp.generated.resources.label_max_hp
import rpg_companion.composeapp.generated.resources.label_race
import rpg_companion.composeapp.generated.resources.label_short_description
import rpg_companion.composeapp.generated.resources.label_skills
import rpg_companion.composeapp.generated.resources.label_walk_speed
import rpg_companion.composeapp.generated.resources.settings_unit_feet_abbr
import rpg_companion.composeapp.generated.resources.settings_unit_meters_abbr

@Composable
internal fun CharacterEditDialog(
    state: CharacterEditState.Loaded,
    shortDescription: String,
    onRaceConfirmed: (Race) -> Unit,
    onClassConfirmed: (Character.Class) -> Unit,
    onLevelConfirmed: (Int) -> Unit,
    onBackgroundConfirmed: (Background?) -> Unit,
    onShortDescriptionConfirmed: (String) -> Unit,
    onStrengthConfirmed: (AbilityScore) -> Unit,
    onDexterityConfirmed: (AbilityScore) -> Unit,
    onConstitutionConfirmed: (AbilityScore) -> Unit,
    onIntelligenceConfirmed: (AbilityScore) -> Unit,
    onWisdomConfirmed: (AbilityScore) -> Unit,
    onCharismaConfirmed: (AbilityScore) -> Unit,
    onArmorClassConfirmed: (Int) -> Unit,
    onMaxHitPointsConfirmed: (Int) -> Unit,
    onWalkSpeedConfirmed: (Int) -> Unit,
    onLanguagesConfirmed: (List<Language>) -> Unit,
    onAlignmentConfirmed: (Creature.Alignment) -> Unit,
    onSkillsConfirmed: (Skills) -> Unit,
    onDismiss: () -> Unit,
) {
    val field = state.editingField ?: return

    when (field) {
        EditingField.ShortDescription -> ShortDescriptionEditDialog(
            initialValue = shortDescription,
            onConfirm = onShortDescriptionConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Background -> SingleChoiceDialog(
            title = stringResource(Res.string.label_background),
            selected = state.character.background,
            options = Background.entries,
            optionLabel = { it.toFormattedString() },
            noneLabel = stringResource(Res.string.background_none),
            onConfirm = onBackgroundConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Level -> NumberStepperDialog(
            title = stringResource(Res.string.label_level),
            initialValue = state.character.level,
            minValue = MIN_CHARACTER_LEVEL,
            maxValue = MAX_CHARACTER_LEVEL,
            onConfirm = onLevelConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Strength -> AbilityEditDialog(
            title = stringResource(Res.string.ability_label_strength),
            initialAbility = state.character.abilities.strength,
            onConfirm = onStrengthConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Dexterity -> AbilityEditDialog(
            title = stringResource(Res.string.ability_label_dexterity),
            initialAbility = state.character.abilities.dexterity,
            onConfirm = onDexterityConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Constitution -> AbilityEditDialog(
            title = stringResource(Res.string.ability_label_constitution),
            initialAbility = state.character.abilities.constitution,
            onConfirm = onConstitutionConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Intelligence -> AbilityEditDialog(
            title = stringResource(Res.string.ability_label_intelligence),
            initialAbility = state.character.abilities.intelligence,
            onConfirm = onIntelligenceConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Wisdom -> AbilityEditDialog(
            title = stringResource(Res.string.ability_label_wisdom),
            initialAbility = state.character.abilities.wisdom,
            onConfirm = onWisdomConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Charisma -> AbilityEditDialog(
            title = stringResource(Res.string.ability_label_charisma),
            initialAbility = state.character.abilities.charisma,
            onConfirm = onCharismaConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.ArmorClass -> NumberStepperDialog(
            title = stringResource(Res.string.label_armor_class),
            initialValue = state.character.armorClass,
            minValue = MIN_ARMOR_CLASS,
            maxValue = MAX_ARMOR_CLASS,
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
                    onConfirm = onWalkSpeedConfirmed,
                    onDismiss = onDismiss,
                )

                DistanceUnit.METERS -> FloatEditDialog(
                    title = title,
                    initialValue = walkFeet.feetToMeters(),
                    onConfirm = { entered -> onWalkSpeedConfirmed(entered.metersToFeet()) },
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

        EditingField.Skills -> SkillSelectDialog(
            current = state.character.skills,
            abilities = state.character.abilities,
            proficiencyBonus = state.character.proficiencyBonus(),
            onConfirm = onSkillsConfirmed,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun ShortDescriptionEditDialog(
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember(initialValue) { mutableStateOf(initialValue) }
    EditDialog(
        title = stringResource(Res.string.label_short_description),
        subtitle = stringResource(Res.string.hint_leave_blank_to_remove),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(text) },
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            placeholder = { Text(stringResource(Res.string.hint_short_description)) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
        )
    }
}

@Composable
private fun NumberStepperDialog(
    title: String,
    initialValue: Int,
    minValue: Int,
    maxValue: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var value by remember(initialValue) { mutableIntStateOf(initialValue.coerceIn(minValue, maxValue)) }
    EditDialog(
        title = title,
        onDismiss = onDismiss,
        onConfirm = { onConfirm(value) },
    ) {
        DecrementIncrementRow(
            value = value,
            minValue = minValue,
            maxValue = maxValue,
            onDecrement = { value-- },
            onIncrement = { value++ },
        )
    }
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
    EditDialog(
        title = title,
        onDismiss = onDismiss,
        onConfirm = { parsedValue?.let(onConfirm) },
        confirmEnabled = parsedValue != null,
    ) {
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
    }
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
    EditDialog(
        title = title,
        onDismiss = onDismiss,
        onConfirm = { parsedValue?.let(onConfirm) },
        confirmEnabled = parsedValue != null,
    ) {
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
    }
}

@Composable
private fun LanguageSelectDialog(
    current: List<Language>,
    onConfirm: (List<Language>) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current.toSet()) }
    EditDialog(
        title = stringResource(Res.string.label_languages),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(selected.toList()) },
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Language.entries.forEach { language ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selected = if (language in selected) selected - language else selected + language
                        }
                        .padding(vertical = spacingCommon),
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
    }
}

@Composable
private fun SkillSelectDialog(
    current: Skills,
    abilities: Abilities,
    proficiencyBonus: Int,
    onConfirm: (Skills) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember {
        mutableStateOf(Skill.entries.filter { it.getProficiency(current) != Proficiency.NONE }.toSet())
    }
    EditDialog(
        title = stringResource(Res.string.label_skills),
        onDismiss = onDismiss,
        onConfirm = {
            val newSkills = Skill.entries.fold(current) { updatedSkills, skill ->
                val wasNonNone = skill.getProficiency(current) != Proficiency.NONE
                val isSelected = skill in selected
                when {
                    isSelected && wasNonNone -> updatedSkills
                    isSelected -> updatedSkills.withProficiency(skill, Proficiency.PROFICIENT)
                    else -> updatedSkills.withProficiency(skill, Proficiency.NONE)
                }
            }
            onConfirm(newSkills)
        },
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Skill.entries.forEach { skill ->
                val prof = skill.getProficiency(current)
                val ability = skill.getRelatedAbilityScore(abilities)
                val modifier = ability.getModifier() + when (prof) {
                    Proficiency.NONE -> 0
                    Proficiency.PROFICIENT -> proficiencyBonus
                    Proficiency.EXPERT -> proficiencyBonus * 2
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selected = if (skill in selected) selected - skill else selected + skill
                        }
                        .padding(vertical = spacingCommon),
                ) {
                    Checkbox(
                        checked = skill in selected,
                        onCheckedChange = null,
                    )
                    Text(
                        text = skill.toFormattedString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = modifier.toSignedString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (skill in selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
