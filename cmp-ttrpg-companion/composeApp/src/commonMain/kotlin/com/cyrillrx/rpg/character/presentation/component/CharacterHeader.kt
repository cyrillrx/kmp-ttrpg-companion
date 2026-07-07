package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import coil3.compose.AsyncImage
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.component.dnd.toSvgPath
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.avatarBorderWidth
import com.cyrillrx.rpg.core.presentation.theme.avatarSize
import com.cyrillrx.rpg.core.presentation.theme.iconSizeLarge
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_level_short

@Composable
internal fun CharacterHeader(
    name: String,
    shortDescription: String,
    race: Race,
    clazz: Character.Class,
    level: Int,
    background: String,
    alignment: Creature.Alignment,
    onNameConfirmed: (String) -> Unit,
    onShortDescriptionTapped: () -> Unit,
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
            InlineEditableText(
                text = name,
                onConfirmed = onNameConfirmed,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
            )
            if (shortDescription.isNotBlank()) {
                Text(
                    text = shortDescription,
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .semantics { role = Role.Button }
                        .clickable(onClick = onShortDescriptionTapped),
                )
            }
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
                    SubtitleChip(alignment.toFormattedString(), onAlignmentTapped)
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
    val iconState by produceState<ClassIconState>(ClassIconState.Loading, clazz) {
        value = resolveClassIconState(clazz) { Res.readBytes(it) }
    }
    val iconColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer
    val borderColor = MaterialTheme.colorScheme.primary
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(avatarSize)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(width = avatarBorderWidth, color = borderColor, shape = CircleShape)
            .clickable(onClick = onClick),
    ) {
        when (val s = iconState) {
            ClassIconState.Loading -> Unit
            is ClassIconState.Loaded -> AsyncImage(
                model = s.bytes,
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier.size(iconSizeLarge),
            )
            ClassIconState.Error -> Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(iconSizeLarge),
            )
            ClassIconState.Unknown -> Icon(
                imageVector = Icons.Filled.QuestionMark,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(iconSizeLarge),
            )
        }
    }
}

internal sealed interface ClassIconState {
    data object Loading : ClassIconState
    data class Loaded(val bytes: ByteArray) : ClassIconState
    data object Error : ClassIconState
    data object Unknown : ClassIconState
}

internal suspend fun resolveClassIconState(
    clazz: Character.Class,
    readBytes: suspend (String) -> ByteArray,
): ClassIconState = if (clazz == Character.Class.UNKNOWN) {
    ClassIconState.Unknown
} else {
    try {
        ClassIconState.Loaded(readBytes(clazz.toSvgPath()))
    } catch (_: Exception) {
        ClassIconState.Error
    }
}

@Composable
private fun InlineEditableText(
    text: String,
    onConfirmed: (String) -> Unit,
    style: TextStyle,
    modifier: Modifier = Modifier,
) {
    var isEditing by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf(TextFieldValue(text, TextRange(text.length))) }
    var hasFocused by remember(isEditing) { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            draft = TextFieldValue(text, TextRange(text.length))
            focusRequester.requestFocus()
        }
    }

    fun commit() {
        if (!isEditing) return

        isEditing = false
        val trimmed = draft.text.trim()
        if (trimmed.isNotBlank() && trimmed != text) onConfirmed(trimmed)
    }

    if (isEditing) {
        BasicTextField(
            value = draft,
            onValueChange = { draft = it },
            textStyle = style.copy(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { commit() }),
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        hasFocused = true
                    } else if (hasFocused) {
                        commit()
                    }
                },
        )
    } else {
        Text(
            text = text,
            style = style,
            modifier = modifier.clickable { isEditing = true },
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

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview
@Composable
private fun PreviewCharacterHeaderLight() {
    AppThemePreview(darkTheme = false) { CharacterHeaderPreview() }
}

@Preview
@Composable
private fun PreviewCharacterHeaderDark() {
    AppThemePreview(darkTheme = true) { CharacterHeaderPreview() }
}

@Composable
private fun CharacterHeaderPreview() {
    val character = SampleCharacterRepository.humanFighter()
    CharacterHeader(
        name = character.name,
        shortDescription = character.translations.values.firstOrNull()?.shortDescription.orEmpty(),
        race = character.race,
        clazz = character.clazz,
        level = character.level,
        background = character.background.toFormattedString(),
        alignment = character.alignment,
        onNameConfirmed = {},
        onShortDescriptionTapped = {},
        onClassTapped = {},
        onRaceTapped = {},
        onLevelTapped = {},
        onBackgroundTapped = {},
        onAlignmentTapped = {},
    )
}
