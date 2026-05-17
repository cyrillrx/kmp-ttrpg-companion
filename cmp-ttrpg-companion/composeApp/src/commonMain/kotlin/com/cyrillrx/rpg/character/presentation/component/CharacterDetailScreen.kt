package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
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
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.component.dnd.toShortString
import com.cyrillrx.rpg.core.presentation.component.dnd.toSvgPath
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.DndGold
import com.cyrillrx.rpg.core.presentation.theme.DndParchment
import com.cyrillrx.rpg.core.presentation.theme.Scarlet
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_cancel
import rpg_companion.composeapp.generated.resources.btn_confirm
import rpg_companion.composeapp.generated.resources.label_abilities
import rpg_companion.composeapp.generated.resources.label_ac
import rpg_companion.composeapp.generated.resources.label_alignment
import rpg_companion.composeapp.generated.resources.label_background
import rpg_companion.composeapp.generated.resources.label_cha
import rpg_companion.composeapp.generated.resources.label_class
import rpg_companion.composeapp.generated.resources.label_combat
import rpg_companion.composeapp.generated.resources.label_con
import rpg_companion.composeapp.generated.resources.label_dex
import rpg_companion.composeapp.generated.resources.label_int
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_level
import rpg_companion.composeapp.generated.resources.label_level_short
import rpg_companion.composeapp.generated.resources.label_max_hp
import rpg_companion.composeapp.generated.resources.label_name
import rpg_companion.composeapp.generated.resources.label_race
import rpg_companion.composeapp.generated.resources.label_str
import rpg_companion.composeapp.generated.resources.label_walk_speed
import rpg_companion.composeapp.generated.resources.label_wis

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterEditViewModel,
    router: CharacterRouter,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val editState = state
    if (editState == null) {
        Scaffold { Loader() }
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
            modifier =
                Modifier
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

// ─── Sheet sections ──────────────────────────────────────────────────────────

@Composable
private fun CharacterHeader(
    name: String,
    race: Race,
    clazz: Character.Class,
    level: Int,
    background: String,
    alignment: Creature.Alignment,
    onNameTapped: () -> Unit,
    onClassTapped: () -> Unit,
    onRaceTapped: () -> Unit,
    onLevelTapped: () -> Unit,
    onBackgroundTapped: () -> Unit,
    onAlignmentTapped: () -> Unit,
) {
    val levelShort = stringResource(Res.string.label_level_short)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingCommon),
        modifier = Modifier.fillMaxWidth(),
    ) {
        ClassIconBox(clazz = clazz, onClick = onClassTapped)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onNameTapped),
            )
            Spacer(Modifier.height(spacingSmall))
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SubtitleChip(race.toFormattedString(), onRaceTapped)
                SubtitleDot()
                SubtitleChip(clazz.toFormattedString(), onClassTapped)
                if (background.isNotBlank()) {
                    SubtitleDot()
                    SubtitleChip(background, onBackgroundTapped)
                }
                if (alignment != Creature.Alignment.UNKNOWN) {
                    SubtitleDot()
                    SubtitleChip(alignment.toShortString(), onAlignmentTapped)
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(onClick = onLevelTapped),
        ) {
            Text(
                text = levelShort.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = level.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ClassIconBox(clazz: Character.Class, onClick: () -> Unit) {
    val svgBytes by produceState<ByteArray?>(null, clazz) {
        value = Res.readBytes(clazz.toSvgPath())
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(DndParchment)
            .border(2.dp, DndGold, CircleShape)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = svgBytes,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Scarlet),
            modifier = Modifier.size(44.dp),
        )
    }
}

@Composable
private fun SubtitleChip(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
private fun SubtitleDot() {
    Text(
        text = "·",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun SheetDivider(label: String) {
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
private fun AbilityGrid(
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
private fun CombatRow(
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
            modifier =
                Modifier
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
private fun WalkSpeedRow(
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
            modifier =
                Modifier
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
private fun LanguagesRow(
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
            Spacer(Modifier.height(spacingSmall))
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

// ─── Dialogs ─────────────────────────────────────────────────────────────────

@Composable
private fun CharacterEditDialog(
    state: CharacterEditState,
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
    onDismiss: () -> Unit,
) {
    val field = state.editingField ?: return

    when (field) {
        EditingField.Name -> TextEditDialog(
            title = stringResource(Res.string.label_name),
            initialValue = state.name,
            onConfirm = onNameConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Background -> TextEditDialog(
            title = stringResource(Res.string.label_background),
            initialValue = state.background,
            onConfirm = onBackgroundConfirmed,
            onDismiss = onDismiss,
            singleLine = false,
        )

        EditingField.Level -> NumberEditDialog(
            title = stringResource(Res.string.label_level),
            initialValue = state.level,
            onConfirm = onLevelConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Strength -> NumberEditDialog(
            title = stringResource(Res.string.label_str),
            initialValue = state.strength,
            onConfirm = onStrengthConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Dexterity -> NumberEditDialog(
            title = stringResource(Res.string.label_dex),
            initialValue = state.dexterity,
            onConfirm = onDexterityConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Constitution -> NumberEditDialog(
            title = stringResource(Res.string.label_con),
            initialValue = state.constitution,
            onConfirm = onConstitutionConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Intelligence -> NumberEditDialog(
            title = stringResource(Res.string.label_int),
            initialValue = state.intelligence,
            onConfirm = onIntelligenceConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Wisdom -> NumberEditDialog(
            title = stringResource(Res.string.label_wis),
            initialValue = state.wisdom,
            onConfirm = onWisdomConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Charisma -> NumberEditDialog(
            title = stringResource(Res.string.label_cha),
            initialValue = state.charisma,
            onConfirm = onCharismaConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.ArmorClass -> NumberEditDialog(
            title = stringResource(Res.string.label_ac),
            initialValue = state.armorClass,
            onConfirm = onArmorClassConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.MaxHitPoints -> NumberEditDialog(
            title = stringResource(Res.string.label_max_hp),
            initialValue = state.maxHitPoints,
            onConfirm = onMaxHitPointsConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.WalkSpeed -> NullableNumberEditDialog(
            title = stringResource(Res.string.label_walk_speed),
            initialValue = state.walkSpeed,
            onConfirm = onWalkSpeedConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Race -> RaceSelectDialog(
            current = state.race,
            onConfirm = onRaceConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Clazz -> ClassSelectDialog(
            current = state.clazz,
            onConfirm = onClassConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Languages -> LanguageSelectDialog(
            current = state.languages,
            onConfirm = onLanguagesConfirmed,
            onDismiss = onDismiss,
        )

        EditingField.Alignment -> AlignmentSelectDialog(
            current = state.alignment,
            onConfirm = onAlignmentConfirmed,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun TextEditDialog(
    title: String,
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    singleLine: Boolean = true,
) {
    var text by remember(title) { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = singleLine,
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

@Composable
private fun NumberEditDialog(
    title: String,
    initialValue: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember(title) { mutableStateOf(initialValue.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
        },
        confirmButton = {
            TextButton(
                onClick = { text.toIntOrNull()?.let(onConfirm) ?: onDismiss() },
            ) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

@Composable
private fun NullableNumberEditDialog(
    title: String,
    initialValue: Int?,
    onConfirm: (Int?) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember(title) { mutableStateOf(initialValue?.toString().orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text.toIntOrNull()) }) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

@Composable
private fun RaceSelectDialog(
    current: Race,
    onConfirm: (Race) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_race)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Race.entries.forEach { race ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        RadioButton(
                            selected = selected == race,
                            onClick = { selected = race },
                        )
                        Text(
                            text = race.toFormattedString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

@Composable
private fun ClassSelectDialog(
    current: Character.Class,
    onConfirm: (Character.Class) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_class)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Character.Class.entries.forEach { clazz ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        RadioButton(
                            selected = selected == clazz,
                            onClick = { selected = clazz },
                        )
                        Text(
                            text = clazz.toFormattedString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

@Composable
private fun AlignmentSelectDialog(
    current: Creature.Alignment,
    onConfirm: (Creature.Alignment) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_alignment)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Creature.Alignment.entries.forEach { alignment ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        RadioButton(
                            selected = selected == alignment,
                            onClick = { selected = alignment },
                        )
                        Text(
                            text = alignment.toFormattedString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

@Composable
private fun LanguageSelectDialog(
    current: List<Language>,
    onConfirm: (List<Language>) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current.toSet()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_languages)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Language.entries.forEach { language ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Checkbox(
                            checked = language in selected,
                            onCheckedChange = { checked ->
                                selected = if (checked) selected + language else selected - language
                            },
                        )
                        Text(
                            text = language.toFormattedString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected.toList()) }) {
                Text(stringResource(Res.string.btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
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
