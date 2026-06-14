package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.presentation.component.StatCell
import com.cyrillrx.rpg.character.presentation.component.StatCellGrid
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.AbilityScore
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.ability_label_cha
import rpg_companion.composeapp.generated.resources.ability_label_con
import rpg_companion.composeapp.generated.resources.ability_label_dex
import rpg_companion.composeapp.generated.resources.ability_label_int
import rpg_companion.composeapp.generated.resources.ability_label_str
import rpg_companion.composeapp.generated.resources.ability_label_wis

@Composable
internal fun AbilitySection(
    abilities: Abilities,
    onStrengthTapped: () -> Unit,
    onDexterityTapped: () -> Unit,
    onConstitutionTapped: () -> Unit,
    onIntelligenceTapped: () -> Unit,
    onWisdomTapped: () -> Unit,
    onCharismaTapped: () -> Unit,
) {
    StatCellGrid(
        columns = 3,
        cells = listOf(
            { modifier ->
                AbilityCard(
                    score = abilities.strength.value,
                    label = stringResource(Res.string.ability_label_str),
                    onClick = onStrengthTapped,
                    modifier = modifier,
                )
            },
            { modifier ->
                AbilityCard(
                    score = abilities.dexterity.value,
                    label = stringResource(Res.string.ability_label_dex),
                    onClick = onDexterityTapped,
                    modifier = modifier,
                )
            },
            { modifier ->
                AbilityCard(
                    score = abilities.constitution.value,
                    label = stringResource(Res.string.ability_label_con),
                    onClick = onConstitutionTapped,
                    modifier = modifier,
                )
            },
            { modifier ->
                AbilityCard(
                    score = abilities.intelligence.value,
                    label = stringResource(Res.string.ability_label_int),
                    onClick = onIntelligenceTapped,
                    modifier = modifier,
                )
            },
            { modifier ->
                AbilityCard(
                    score = abilities.wisdom.value,
                    label = stringResource(Res.string.ability_label_wis),
                    onClick = onWisdomTapped,
                    modifier = modifier,
                )
            },
            { modifier ->
                AbilityCard(
                    score = abilities.charisma.value,
                    label = stringResource(Res.string.ability_label_cha),
                    onClick = onCharismaTapped,
                    modifier = modifier,
                )
            },
        ),
    )
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
        caption = AbilityScore(score).getModifier().toSignedString(),
        onClick = onClick,
        modifier = modifier,
    )
}
