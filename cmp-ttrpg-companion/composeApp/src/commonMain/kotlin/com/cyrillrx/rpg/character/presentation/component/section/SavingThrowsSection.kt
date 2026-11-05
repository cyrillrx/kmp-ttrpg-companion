package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.savingThrowModifier
import com.cyrillrx.rpg.character.domain.savingThrowProficiency
import com.cyrillrx.rpg.character.presentation.component.StatCell
import com.cyrillrx.rpg.character.presentation.component.StatCellGrid
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.toAbbreviationString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Proficiency
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SavingThrowsSection(
    character: Character,
    modifier: Modifier = Modifier,
) {
    StatCellGrid(
        columns = 3,
        cells = Ability.entries.map { ability ->
            @Composable { cellModifier: Modifier -> SavingThrowCard(character, ability, cellModifier) }
        },
        modifier = modifier,
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
        valueColor = character.savingThrowProficiency(ability).getColor(),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PreviewSavingThrowsSectionLight() {
    AppThemePreview(darkTheme = false) {
        SavingThrowsSection(character = previewCharacter())
    }
}

@Preview
@Composable
private fun PreviewSavingThrowsSectionDark() {
    AppThemePreview(darkTheme = true) {
        SavingThrowsSection(character = previewCharacter())
    }
}

private fun previewCharacter(): Character {
    val rogue = SampleCharacterRepository.elfRogue()
    val abilities = rogue.abilities.copy(
        dexterity = AbilityScore(16, savingThrowProficiency = Proficiency.PROFICIENT),
        intelligence = AbilityScore(14, savingThrowProficiency = Proficiency.PROFICIENT),
    )
    return rogue.copy(abilities = abilities)
}
