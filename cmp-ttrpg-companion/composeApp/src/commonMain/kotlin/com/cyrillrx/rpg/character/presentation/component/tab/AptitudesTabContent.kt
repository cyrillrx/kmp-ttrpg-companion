package com.cyrillrx.rpg.character.presentation.component.tab

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.component.section.AbilitySection
import com.cyrillrx.rpg.character.presentation.component.section.SavingThrowsSection
import com.cyrillrx.rpg.character.presentation.component.section.SheetDivider
import com.cyrillrx.rpg.character.presentation.component.section.SkillsSection
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_abilities
import rpg_companion.composeapp.generated.resources.label_saving_throws
import rpg_companion.composeapp.generated.resources.label_skills

@Composable
internal fun AptitudesTabContent(
    state: CharacterEditState.Loaded,
    onFieldTapped: (EditingField) -> Unit,
) {
    SheetDivider(stringResource(Res.string.label_abilities))

    AbilitySection(
        abilities = state.character.abilities,
        onStrengthTapped = { onFieldTapped(EditingField.Strength) },
        onDexterityTapped = { onFieldTapped(EditingField.Dexterity) },
        onConstitutionTapped = { onFieldTapped(EditingField.Constitution) },
        onIntelligenceTapped = { onFieldTapped(EditingField.Intelligence) },
        onWisdomTapped = { onFieldTapped(EditingField.Wisdom) },
        onCharismaTapped = { onFieldTapped(EditingField.Charisma) },
    )

    SheetDivider(stringResource(Res.string.label_saving_throws))

    SavingThrowsSection(
        abilities = state.character.abilities,
        proficiencyBonus = state.character.proficiencyBonus(),
    )

    SheetDivider(stringResource(Res.string.label_skills))

    SkillsSection(
        skills = state.character.skills,
        abilities = state.character.abilities,
        proficiencyBonus = state.character.proficiencyBonus(),
        onTap = { onFieldTapped(EditingField.Skills) },
    )
}
