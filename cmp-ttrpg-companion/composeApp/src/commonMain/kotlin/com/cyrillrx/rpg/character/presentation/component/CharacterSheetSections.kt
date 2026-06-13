package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.LocalDistanceUnit
import com.cyrillrx.rpg.core.presentation.component.dnd.toDistanceString
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.ability_label_cha
import rpg_companion.composeapp.generated.resources.ability_label_con
import rpg_companion.composeapp.generated.resources.ability_label_dex
import rpg_companion.composeapp.generated.resources.ability_label_int
import rpg_companion.composeapp.generated.resources.ability_label_str
import rpg_companion.composeapp.generated.resources.ability_label_wis
import rpg_companion.composeapp.generated.resources.label_ac
import rpg_companion.composeapp.generated.resources.label_initiative
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_max_hp
import rpg_companion.composeapp.generated.resources.label_walk_speed
import rpg_companion.composeapp.generated.resources.settings_unit_feet_abbr
import rpg_companion.composeapp.generated.resources.settings_unit_meters_abbr

@Composable
internal fun SheetDivider(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = Modifier.fillMaxWidth(),
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
internal fun AbilityGrid(
    abilities: Abilities,
    onStrengthTapped: () -> Unit,
    onDexterityTapped: () -> Unit,
    onConstitutionTapped: () -> Unit,
    onIntelligenceTapped: () -> Unit,
    onWisdomTapped: () -> Unit,
    onCharismaTapped: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingMedium)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth(),
        ) {
            AbilityCard(
                score = abilities.strength.value,
                label = stringResource(Res.string.ability_label_str),
                onClick = onStrengthTapped,
                modifier = Modifier.weight(1f),
            )
            AbilityCard(
                score = abilities.dexterity.value,
                label = stringResource(Res.string.ability_label_dex),
                onClick = onDexterityTapped,
                modifier = Modifier.weight(1f),
            )
            AbilityCard(
                score = abilities.constitution.value,
                label = stringResource(Res.string.ability_label_con),
                onClick = onConstitutionTapped,
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth(),
        ) {
            AbilityCard(
                score = abilities.intelligence.value,
                label = stringResource(Res.string.ability_label_int),
                onClick = onIntelligenceTapped,
                modifier = Modifier.weight(1f),
            )
            AbilityCard(
                score = abilities.wisdom.value,
                label = stringResource(Res.string.ability_label_wis),
                onClick = onWisdomTapped,
                modifier = Modifier.weight(1f),
            )
            AbilityCard(
                score = abilities.charisma.value,
                label = stringResource(Res.string.ability_label_cha),
                onClick = onCharismaTapped,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun AbilityCard(
    score: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatCell(
        label = label,
        value = score.toString(),
        caption = Ability(score).getModifier().toSignedString(),
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
internal fun SavingThrowsSection(
    abilities: Abilities,
    proficiencyBonus: Int,
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingMedium)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth(),
        ) {
            SavingThrowCard(
                label = stringResource(Res.string.ability_label_str),
                ability = abilities.strength,
                proficiencyBonus = proficiencyBonus,
                modifier = Modifier.weight(1f),
            )
            SavingThrowCard(
                label = stringResource(Res.string.ability_label_dex),
                ability = abilities.dexterity,
                proficiencyBonus = proficiencyBonus,
                modifier = Modifier.weight(1f),
            )
            SavingThrowCard(
                label = stringResource(Res.string.ability_label_con),
                ability = abilities.constitution,
                proficiencyBonus = proficiencyBonus,
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth(),
        ) {
            SavingThrowCard(
                label = stringResource(Res.string.ability_label_int),
                ability = abilities.intelligence,
                proficiencyBonus = proficiencyBonus,
                modifier = Modifier.weight(1f),
            )
            SavingThrowCard(
                label = stringResource(Res.string.ability_label_wis),
                ability = abilities.wisdom,
                proficiencyBonus = proficiencyBonus,
                modifier = Modifier.weight(1f),
            )
            SavingThrowCard(
                label = stringResource(Res.string.ability_label_cha),
                ability = abilities.charisma,
                proficiencyBonus = proficiencyBonus,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SavingThrowCard(
    label: String,
    ability: Ability,
    proficiencyBonus: Int,
    modifier: Modifier = Modifier,
) {
    val isProficient = ability.savingThrowProficiency != Proficiency.NONE
    StatCell(
        label = label,
        value = ability.getSavingThrow(proficiencyBonus).toSignedString(),
        valueColor = if (isProficient) MaterialTheme.colorScheme.primary else Color.Unspecified,
        modifier = modifier,
    )
}

@Composable
internal fun CombatRow(
    armorClass: Int,
    initiative: Int,
    maxHitPoints: Int,
    onAcTapped: () -> Unit,
    onMaxHpTapped: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = Modifier.fillMaxWidth(),
    ) {
        StatCell(
            label = stringResource(Res.string.label_ac),
            value = armorClass.toString(),
            onClick = onAcTapped,
            modifier = Modifier.weight(1f),
        )
        StatCell(
            label = stringResource(Res.string.label_initiative),
            value = initiative.toSignedString(),
            modifier = Modifier.weight(1f),
        )
        StatCell(
            label = stringResource(Res.string.label_max_hp),
            value = maxHitPoints.toString(),
            onClick = onMaxHpTapped,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
internal fun WalkSpeedRow(
    walkSpeed: Int,
    onTap: () -> Unit,
) {
    Card(
        onClick = onTap,
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingCommon, vertical = spacingMedium),
        ) {
            Text(
                text = stringResource(
                    Res.string.label_walk_speed,
                    stringResource(
                        when (LocalDistanceUnit.current) {
                            DistanceUnit.FEET -> Res.string.settings_unit_feet_abbr
                            DistanceUnit.METERS -> Res.string.settings_unit_meters_abbr
                        },
                    ),
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.5f),
            )
            Text(
                text = walkSpeed.toDistanceString(LocalDistanceUnit.current),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(0.5f),
            )
        }
    }
}

@Composable
internal fun LanguagesRow(
    languages: List<Language>,
    onTap: () -> Unit,
) {
    Card(
        onClick = onTap,
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingCommon, vertical = spacingMedium),
        ) {
            Text(
                text = stringResource(Res.string.label_languages),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (languages.isEmpty()) {
                Text(
                    text = "—",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    text = languages.map { it.toFormattedString() }.joinToString(" · "),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
