package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.savingThrowModifier
import com.cyrillrx.rpg.character.presentation.component.StatCell
import com.cyrillrx.rpg.character.presentation.component.StatCellGrid
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.toAbbreviationString
import com.cyrillrx.rpg.creature.domain.Ability

@Composable
internal fun SavingThrowsSection(character: Character) {
    StatCellGrid(
        columns = 3,
        cells = Ability.entries.map { ability ->
            @Composable { modifier: Modifier -> SavingThrowCard(character, ability, modifier) }
        },
    )
}

@Composable
private fun SavingThrowCard(
    character: Character,
    ability: Ability,
    modifier: Modifier = Modifier,
) {
    StatCell(
        label = ability.toAbbreviationString(),
        value = character.savingThrowModifier(ability).toSignedString(),
        valueColor = ability.toAbilityScore(character.abilities).savingThrowProficiency.getColor(),
        modifier = modifier,
    )
}
