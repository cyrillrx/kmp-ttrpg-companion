package com.cyrillrx.rpg.character.presentation.component.tab

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.component.section.AbilitySection
import com.cyrillrx.rpg.character.presentation.component.section.SavingThrowsSection
import com.cyrillrx.rpg.character.presentation.component.section.SheetDivider
import com.cyrillrx.rpg.character.presentation.component.section.SkillsSection
import org.jetbrains.compose.resources.stringResource
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.btn_edit_skills
import rpg_companion.app.shared.ui.generated.resources.label_abilities
import rpg_companion.app.shared.ui.generated.resources.label_saving_throws
import rpg_companion.app.shared.ui.generated.resources.label_skills

@Composable
internal fun AptitudesTabContent(
    state: CharacterEditState.Loaded,
    onFieldTapped: (EditingField) -> Unit,
    onCheckTapped: (D20Check) -> Unit,
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
        character = state.character,
        onSavingThrowTapped = { ability -> onCheckTapped(D20Check.SavingThrow(ability)) },
    )

    SheetDivider(
        label = stringResource(Res.string.label_skills),
        onEditClicked = { onFieldTapped(EditingField.Skills) },
        editLabel = stringResource(Res.string.btn_edit_skills),
    )

    SkillsSection(
        character = state.character,
        onSkillTapped = { skill -> onCheckTapped(D20Check.SkillCheck(skill)) },
    )
}
