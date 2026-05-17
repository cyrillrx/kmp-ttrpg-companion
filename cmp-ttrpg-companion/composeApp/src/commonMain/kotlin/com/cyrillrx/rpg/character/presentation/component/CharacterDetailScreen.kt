package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.EditingField
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterEditViewModel
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_abilities
import rpg_companion.composeapp.generated.resources.label_combat
import rpg_companion.composeapp.generated.resources.label_languages

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterEditViewModel,
    router: CharacterRouter,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val editState = state
    if (editState == null) {
        Scaffold { innerPadding -> Loader(modifier = Modifier.padding(innerPadding)) }
        return
    }
    CharacterDetailScreen(
        state = editState,
        onFieldTapped = viewModel::editField,
        onNameConfirmed = viewModel::saveName,
        onRaceConfirmed = viewModel::saveRace,
        onClassConfirmed = viewModel::saveClass,
        onLevelConfirmed = viewModel::saveLevel,
        onBackgroundConfirmed = viewModel::saveBackground,
        onStrengthConfirmed = viewModel::saveStrength,
        onDexterityConfirmed = viewModel::saveDexterity,
        onConstitutionConfirmed = viewModel::saveConstitution,
        onIntelligenceConfirmed = viewModel::saveIntelligence,
        onWisdomConfirmed = viewModel::saveWisdom,
        onCharismaConfirmed = viewModel::saveCharisma,
        onArmorClassConfirmed = viewModel::saveArmorClass,
        onMaxHitPointsConfirmed = viewModel::saveMaxHitPoints,
        onWalkSpeedConfirmed = viewModel::saveWalkSpeed,
        onLanguagesConfirmed = viewModel::saveLanguages,
        onAlignmentConfirmed = viewModel::saveAlignment,
        onDialogDismissed = viewModel::cancelEditing,
        onNavigateUpClicked = router::navigateUp,
    )
}

@Composable
fun CharacterDetailScreen(
    state: CharacterEditState,
    onFieldTapped: (EditingField) -> Unit,
    onNameConfirmed: (String) -> Unit,
    onRaceConfirmed: (Race) -> Unit,
    onClassConfirmed: (Character.Class) -> Unit,
    onLevelConfirmed: (Int) -> Unit,
    onBackgroundConfirmed: (String) -> Unit,
    onStrengthConfirmed: (Int) -> Unit,
    onDexterityConfirmed: (Int) -> Unit,
    onConstitutionConfirmed: (Int) -> Unit,
    onIntelligenceConfirmed: (Int) -> Unit,
    onWisdomConfirmed: (Int) -> Unit,
    onCharismaConfirmed: (Int) -> Unit,
    onArmorClassConfirmed: (Int) -> Unit,
    onMaxHitPointsConfirmed: (Int) -> Unit,
    onWalkSpeedConfirmed: (Int?) -> Unit,
    onLanguagesConfirmed: (List<Language>) -> Unit,
    onAlignmentConfirmed: (Creature.Alignment) -> Unit,
    onDialogDismissed: () -> Unit,
    onNavigateUpClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "",
                onNavigateUpClicked = onNavigateUpClicked,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(spacingCommon),
            verticalArrangement = Arrangement.spacedBy(spacingMedium),
        ) {
            CharacterHeader(
                name = state.name,
                race = state.race,
                clazz = state.clazz,
                level = state.level,
                background = state.background,
                alignment = state.alignment,
                onNameTapped = { onFieldTapped(EditingField.Name) },
                onClassTapped = { onFieldTapped(EditingField.Clazz) },
                onRaceTapped = { onFieldTapped(EditingField.Race) },
                onLevelTapped = { onFieldTapped(EditingField.Level) },
                onBackgroundTapped = { onFieldTapped(EditingField.Background) },
                onAlignmentTapped = { onFieldTapped(EditingField.Alignment) },
            )

            SheetDivider(stringResource(Res.string.label_abilities))

            AbilityGrid(
                strength = state.strength,
                dexterity = state.dexterity,
                constitution = state.constitution,
                intelligence = state.intelligence,
                wisdom = state.wisdom,
                charisma = state.charisma,
                onStrengthTapped = { onFieldTapped(EditingField.Strength) },
                onDexterityTapped = { onFieldTapped(EditingField.Dexterity) },
                onConstitutionTapped = { onFieldTapped(EditingField.Constitution) },
                onIntelligenceTapped = { onFieldTapped(EditingField.Intelligence) },
                onWisdomTapped = { onFieldTapped(EditingField.Wisdom) },
                onCharismaTapped = { onFieldTapped(EditingField.Charisma) },
            )

            SheetDivider(stringResource(Res.string.label_combat))

            CombatRow(
                armorClass = state.armorClass,
                maxHitPoints = state.maxHitPoints,
                onAcTapped = { onFieldTapped(EditingField.ArmorClass) },
                onMaxHpTapped = { onFieldTapped(EditingField.MaxHitPoints) },
            )

            WalkSpeedRow(
                walkSpeed = state.walkSpeed,
                onTap = { onFieldTapped(EditingField.WalkSpeed) },
            )

            SheetDivider(stringResource(Res.string.label_languages))

            LanguagesRow(
                languages = state.languages,
                onTap = { onFieldTapped(EditingField.Languages) },
            )

            Spacer(Modifier.height(spacingCommon))
        }
    }

    CharacterEditDialog(
        state = state,
        onNameConfirmed = onNameConfirmed,
        onRaceConfirmed = onRaceConfirmed,
        onClassConfirmed = onClassConfirmed,
        onLevelConfirmed = onLevelConfirmed,
        onBackgroundConfirmed = onBackgroundConfirmed,
        onStrengthConfirmed = onStrengthConfirmed,
        onDexterityConfirmed = onDexterityConfirmed,
        onConstitutionConfirmed = onConstitutionConfirmed,
        onIntelligenceConfirmed = onIntelligenceConfirmed,
        onWisdomConfirmed = onWisdomConfirmed,
        onCharismaConfirmed = onCharismaConfirmed,
        onArmorClassConfirmed = onArmorClassConfirmed,
        onMaxHitPointsConfirmed = onMaxHitPointsConfirmed,
        onWalkSpeedConfirmed = onWalkSpeedConfirmed,
        onLanguagesConfirmed = onLanguagesConfirmed,
        onAlignmentConfirmed = onAlignmentConfirmed,
        onDismiss = onDialogDismissed,
    )
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview
@Composable
private fun PreviewCharacterDetailScreenLight() {
    AppThemePreview(darkTheme = false) { CharacterDetailScreenPreview() }
}

@Preview
@Composable
private fun PreviewCharacterDetailScreenDark() {
    AppThemePreview(darkTheme = true) { CharacterDetailScreenPreview() }
}

@Composable
private fun CharacterDetailScreenPreview() {
    CharacterDetailScreen(
        state = CharacterEditState.from(SampleCharacterRepository.humanFighter()),
        onFieldTapped = {},
        onNameConfirmed = {},
        onRaceConfirmed = {},
        onClassConfirmed = {},
        onLevelConfirmed = {},
        onBackgroundConfirmed = {},
        onStrengthConfirmed = {},
        onDexterityConfirmed = {},
        onConstitutionConfirmed = {},
        onIntelligenceConfirmed = {},
        onWisdomConfirmed = {},
        onCharismaConfirmed = {},
        onArmorClassConfirmed = {},
        onMaxHitPointsConfirmed = {},
        onWalkSpeedConfirmed = {},
        onLanguagesConfirmed = {},
        onAlignmentConfirmed = {},
        onDialogDismissed = {},
        onNavigateUpClicked = {},
    )
}
