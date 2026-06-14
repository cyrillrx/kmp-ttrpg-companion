package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cyrillrx.rpg.character.presentation.component.StatCell
import com.cyrillrx.rpg.character.presentation.component.StatCellGrid
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Proficiency
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.ability_label_cha
import rpg_companion.composeapp.generated.resources.ability_label_con
import rpg_companion.composeapp.generated.resources.ability_label_dex
import rpg_companion.composeapp.generated.resources.ability_label_int
import rpg_companion.composeapp.generated.resources.ability_label_str
import rpg_companion.composeapp.generated.resources.ability_label_wis

@Composable
internal fun SavingThrowsSection(
    abilities: Abilities,
    proficiencyBonus: Int,
) {
    StatCellGrid(
        columns = 3,
        cells = listOf(
            { modifier ->
                SavingThrowCard(
                    label = stringResource(Res.string.ability_label_str),
                    ability = abilities.strength,
                    proficiencyBonus = proficiencyBonus,
                    modifier = modifier,
                )
            },
            { modifier ->
                SavingThrowCard(
                    label = stringResource(Res.string.ability_label_dex),
                    ability = abilities.dexterity,
                    proficiencyBonus = proficiencyBonus,
                    modifier = modifier,
                )
            },
            { modifier ->
                SavingThrowCard(
                    label = stringResource(Res.string.ability_label_con),
                    ability = abilities.constitution,
                    proficiencyBonus = proficiencyBonus,
                    modifier = modifier,
                )
            },
            { modifier ->
                SavingThrowCard(
                    label = stringResource(Res.string.ability_label_int),
                    ability = abilities.intelligence,
                    proficiencyBonus = proficiencyBonus,
                    modifier = modifier,
                )
            },
            { modifier ->
                SavingThrowCard(
                    label = stringResource(Res.string.ability_label_wis),
                    ability = abilities.wisdom,
                    proficiencyBonus = proficiencyBonus,
                    modifier = modifier,
                )
            },
            { modifier ->
                SavingThrowCard(
                    label = stringResource(Res.string.ability_label_cha),
                    ability = abilities.charisma,
                    proficiencyBonus = proficiencyBonus,
                    modifier = modifier,
                )
            },
        ),
    )
}

@Composable
private fun SavingThrowCard(
    label: String,
    ability: AbilityScore,
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
