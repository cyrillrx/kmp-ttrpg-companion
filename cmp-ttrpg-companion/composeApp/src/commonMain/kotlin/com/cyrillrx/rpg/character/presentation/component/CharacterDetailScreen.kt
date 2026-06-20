package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
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
import com.cyrillrx.rpg.character.presentation.component.section.AbilitySection
import com.cyrillrx.rpg.character.presentation.component.section.CombatRow
import com.cyrillrx.rpg.character.presentation.component.section.LanguagesRow
import com.cyrillrx.rpg.character.presentation.component.section.SavingThrowsSection
import com.cyrillrx.rpg.character.presentation.component.section.SheetDivider
import com.cyrillrx.rpg.character.presentation.component.section.SkillsSection
import com.cyrillrx.rpg.character.presentation.component.section.WalkSpeedRow
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
import rpg_companion.composeapp.generated.resources.label_abilities
import rpg_companion.composeapp.generated.resources.label_combat
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_profile
import rpg_companion.composeapp.generated.resources.label_saving_throws
import rpg_companion.composeapp.generated.resources.label_skills
import kotlin.math.roundToInt

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
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    // Collapsing header state, driven by the nested scroll of the pager content.
    var expandableHeightPx by remember { mutableStateOf(0) }
    var collapse by remember { mutableStateOf(0f) }
    val collapsedFraction = if (expandableHeightPx == 0) 0f else collapse / expandableHeightPx
    val collapseConnection = remember {
        object : NestedScrollConnection {
            private fun consume(deltaY: Float): Offset {
                val previous = collapse
                collapse = (collapse - deltaY).coerceIn(0f, expandableHeightPx.toFloat())
                return Offset(0f, -(collapse - previous))
            }

            // Collapse first when scrolling up.
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                if (available.y < 0f) consume(available.y) else Offset.Zero

            // Re-expand only once the content has reached its top.
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset =
                if (available.y > 0f) consume(available.y) else Offset.Zero
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pointerInput(focusManager) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .nestedScroll(collapseConnection),
        ) {
            // Pinned compact bar: back button, plus the name fading in once collapsed.
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
                Text(
                    text = state.character.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.alpha(collapsedFraction),
                )
            }

            // Rich identity header; its measured height shrinks with `collapse`.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds()
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        expandableHeightPx = placeable.height
                        val collapsePx = collapse.roundToInt().coerceIn(0, placeable.height)
                        layout(placeable.width, placeable.height - collapsePx) {
                            placeable.place(0, -collapsePx)
                        }
                    },
            ) {
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
            }

            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                    text = { Text(stringResource(Res.string.label_abilities)) },
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                    text = { Text(stringResource(Res.string.label_combat)) },
                )
                Tab(
                    selected = pagerState.currentPage == 2,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(2) } },
                    text = { Text(stringResource(Res.string.label_profile)) },
                )
            }

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
                    when (page) {
                        0 -> AbilitiesTabContent(state, onFieldTapped)
                        1 -> CombatTabContent(state, onFieldTapped)
                        else -> ProfileTabContent(state, onFieldTapped)
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

// ─── Tab content ─────────────────────────────────────────────────────────────

@Composable
private fun CombatTabContent(
    state: CharacterEditState.Loaded,
    onFieldTapped: (EditingField) -> Unit,
) {
    CombatRow(
        armorClass = state.character.armorClass,
        initiative = state.character.initiativeModifier(),
        maxHitPoints = state.character.maxHitPoints,
        onArmorClassTapped = { onFieldTapped(EditingField.ArmorClass) },
        onMaxHitPointsTapped = { onFieldTapped(EditingField.MaxHitPoints) },
    )

    WalkSpeedRow(
        walkSpeed = state.character.speeds.walk,
        onTap = { onFieldTapped(EditingField.WalkSpeed) },
    )
}

@Composable
private fun AbilitiesTabContent(
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

@Composable
private fun ProfileTabContent(
    state: CharacterEditState.Loaded,
    onFieldTapped: (EditingField) -> Unit,
) {
    SheetDivider(stringResource(Res.string.label_languages))

    LanguagesRow(
        languages = state.character.languages,
        onTap = { onFieldTapped(EditingField.Languages) },
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
