package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.domain.Ability
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_ac
import rpg_companion.composeapp.generated.resources.label_cha
import rpg_companion.composeapp.generated.resources.label_con
import rpg_companion.composeapp.generated.resources.label_dex
import rpg_companion.composeapp.generated.resources.label_int
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_max_hp
import rpg_companion.composeapp.generated.resources.label_str
import rpg_companion.composeapp.generated.resources.label_walk_speed
import rpg_companion.composeapp.generated.resources.label_wis

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
    strength: Int,
    dexterity: Int,
    constitution: Int,
    intelligence: Int,
    wisdom: Int,
    charisma: Int,
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
                score = strength,
                label = stringResource(Res.string.label_str),
                onClick = onStrengthTapped,
                modifier = Modifier.weight(1f),
            )
            AbilityCard(
                score = dexterity,
                label = stringResource(Res.string.label_dex),
                onClick = onDexterityTapped,
                modifier = Modifier.weight(1f),
            )
            AbilityCard(
                score = constitution,
                label = stringResource(Res.string.label_con),
                onClick = onConstitutionTapped,
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth(),
        ) {
            AbilityCard(
                score = intelligence,
                label = stringResource(Res.string.label_int),
                onClick = onIntelligenceTapped,
                modifier = Modifier.weight(1f),
            )
            AbilityCard(
                score = wisdom,
                label = stringResource(Res.string.label_wis),
                onClick = onWisdomTapped,
                modifier = Modifier.weight(1f),
            )
            AbilityCard(
                score = charisma,
                label = stringResource(Res.string.label_cha),
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
    val modifier_ = Ability(score).getModifier()
    val modifierText = if (modifier_ >= 0) "+$modifier_" else "$modifier_"

    ElevatedCard(
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacingMedium, horizontal = spacingSmall),
        ) {
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = modifierText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
internal fun CombatRow(
    armorClass: Int,
    maxHitPoints: Int,
    onAcTapped: () -> Unit,
    onMaxHpTapped: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = Modifier.fillMaxWidth(),
    ) {
        StatCard(
            label = stringResource(Res.string.label_ac),
            value = armorClass.toString(),
            onClick = onAcTapped,
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = stringResource(Res.string.label_max_hp),
            value = maxHitPoints.toString(),
            onClick = onMaxHpTapped,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
internal fun WalkSpeedRow(
    walkSpeed: Int?,
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
                text = stringResource(Res.string.label_walk_speed),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.5f),
            )
            Text(
                text = walkSpeed?.toString() ?: "—",
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
