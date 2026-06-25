package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.CoercedValue
import com.cyrillrx.rpg.character.presentation.component.dialog.CharacterEditDialog
import com.cyrillrx.rpg.character.presentation.component.tab.AptitudesTabContent
import com.cyrillrx.rpg.character.presentation.component.tab.CharacterSheetTab
import com.cyrillrx.rpg.character.presentation.component.tab.CombatTabContent
import com.cyrillrx.rpg.character.presentation.component.tab.ProfileTabContent
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterEditViewModel
import com.cyrillrx.rpg.core.presentation.LocalDistanceUnit
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.dnd.toDistanceString
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.topAppBarHeight
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Skills
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_back
import rpg_companion.composeapp.generated.resources.character_not_found
import rpg_companion.composeapp.generated.resources.info_value_coerced

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterEditViewModel,
    router: CharacterRouter,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val distanceUnit by rememberUpdatedState(LocalDistanceUnit.current)
    LaunchedEffect(viewModel) {
        viewModel.coercedValueEvent.collect { event ->
            val (from, to) = when (event) {
                is CoercedValue.Numeric -> event.original.toString() to event.coerced.toString()
                is CoercedValue.Distance ->
                    event.originalFeet.toDistanceString(distanceUnit) to
                        event.coercedFeet.toDistanceString(distanceUnit)
            }
            val message = getString(Res.string.info_value_coerced, from, to)
            snackbarHostState.showSnackbar(message)
        }
    }
    when (val s = state) {
        CharacterEditState.Loading -> Scaffold { innerPadding ->
            Loader(modifier = Modifier.padding(innerPadding))
        }

        is CharacterEditState.NotFound -> Scaffold { innerPadding ->
            ErrorLayout(
                errorMessage = stringResource(Res.string.character_not_found, s.characterId),
                modifier = Modifier.padding(innerPadding),
            )
        }

        is CharacterEditState.Loaded -> CharacterDetailScreen(
            state = s,
            snackbarHostState = snackbarHostState,
            onFieldTapped = viewModel::editField,
            onNameConfirmed = viewModel::saveName,
            onShortDescriptionTapped = { viewModel.editField(EditingField.ShortDescription) },
            onShortDescriptionConfirmed = viewModel::saveShortDescription,
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
            onSkillsConfirmed = viewModel::saveSkills,
            onDialogDismissed = viewModel::cancelEditing,
            onNavigateUpClicked = router::navigateUp,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    state: CharacterEditState.Loaded,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onFieldTapped: (EditingField) -> Unit,
    onNameConfirmed: (String) -> Unit,
    onShortDescriptionTapped: () -> Unit,
    onShortDescriptionConfirmed: (String) -> Unit,
    onRaceConfirmed: (Race) -> Unit,
    onClassConfirmed: (Character.Class) -> Unit,
    onLevelConfirmed: (Int) -> Unit,
    onBackgroundConfirmed: (Background?) -> Unit,
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
    onDialogDismissed: () -> Unit,
    onNavigateUpClicked: () -> Unit,
) {
    val locale = currentLocale()
    val shortDescription = state.character.resolveTranslation(locale)?.shortDescription.orEmpty()
    val pagerState = rememberPagerState(pageCount = { CharacterSheetTab.entries.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pointerInput(focusManager) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        ) {
            // Pinned compact bar: back button only (the name lives in the header below).
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(topAppBarHeight),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onNavigateUpClicked) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(Res.string.btn_back),
                    )
                }
            }

            // Pinned rich identity header.
            Column(
                modifier = Modifier
                    .padding(horizontal = spacingCommon)
                    .padding(bottom = spacingMedium),
            ) {
                CharacterHeader(
                    name = state.character.name,
                    shortDescription = shortDescription,
                    race = state.character.race,
                    clazz = state.character.clazz,
                    level = state.character.level,
                    background = state.character.background.toFormattedString(),
                    alignment = state.character.alignment,
                    onNameConfirmed = onNameConfirmed,
                    onShortDescriptionTapped = onShortDescriptionTapped,
                    onClassTapped = { onFieldTapped(EditingField.Clazz) },
                    onRaceTapped = { onFieldTapped(EditingField.Race) },
                    onLevelTapped = { onFieldTapped(EditingField.Level) },
                    onBackgroundTapped = { onFieldTapped(EditingField.Background) },
                    onAlignmentTapped = { onFieldTapped(EditingField.Alignment) },
                )
            }

            // Pinned tab row, kept in sync with the pager.
            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                CharacterSheetTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(stringResource(tab.label)) },
                    )
                }
            }

            // Only the tab content scrolls (vertically) and swipes (horizontally).
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(spacingCommon),
                    verticalArrangement = Arrangement.spacedBy(spacingMedium),
                ) {
                    when (CharacterSheetTab.entries[page]) {
                        CharacterSheetTab.APTITUDES -> AptitudesTabContent(state, onFieldTapped)
                        CharacterSheetTab.COMBAT -> CombatTabContent(state, onFieldTapped)
                        CharacterSheetTab.PROFILE -> ProfileTabContent(state, onFieldTapped)
                    }

                    Spacer(Modifier.height(spacingCommon))
                }
            }
        }
    }

    CharacterEditDialog(
        state = state,
        shortDescription = shortDescription,
        onRaceConfirmed = onRaceConfirmed,
        onClassConfirmed = onClassConfirmed,
        onLevelConfirmed = onLevelConfirmed,
        onBackgroundConfirmed = onBackgroundConfirmed,
        onShortDescriptionConfirmed = onShortDescriptionConfirmed,
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
        onSkillsConfirmed = onSkillsConfirmed,
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
        state = CharacterEditState.Loaded(SampleCharacterRepository.humanFighter()),
        onFieldTapped = {},
        onNameConfirmed = {},
        onShortDescriptionTapped = {},
        onShortDescriptionConfirmed = {},
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
        onSkillsConfirmed = {},
        onDialogDismissed = {},
        onNavigateUpClicked = {},
    )
}
