package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.ClassLevels
import com.cyrillrx.rpg.character.domain.MIN_CHARACTER_LEVEL
import com.cyrillrx.rpg.character.domain.canAddClass
import com.cyrillrx.rpg.character.domain.maxLevelFor
import com.cyrillrx.rpg.character.domain.withClassAdded
import com.cyrillrx.rpg.character.domain.withClassLevel
import com.cyrillrx.rpg.character.domain.withClassRemoved
import com.cyrillrx.rpg.character.presentation.component.ClassIcon
import com.cyrillrx.rpg.core.presentation.component.dialog.EditDialog
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMedium
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMediumLarge
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_class
import rpg_companion.composeapp.generated.resources.btn_remove_class
import rpg_companion.composeapp.generated.resources.label_classes
import rpg_companion.composeapp.generated.resources.label_primary_class

@Composable
internal fun ClassEditorDialog(
    character: Character,
    onConfirm: (ClassLevels, Character.Class) -> Unit,
    onDismiss: () -> Unit,
) {
    var working by remember(character) { mutableStateOf(character) }
    var showPicker by remember { mutableStateOf(false) }

    if (showPicker) {
        ClassPickerDialog(
            taken = working.classes.keys,
            onPick = {
                working = working.withClassAdded(it)
                showPicker = false
            },
            onDismiss = { showPicker = false },
        )
        return
    }

    val assigned = working.classes.filterKeys { it != Character.Class.UNKNOWN }.toList()
    val untakenExists = (Character.Class.entries - working.classes.keys - Character.Class.UNKNOWN).isNotEmpty()

    EditDialog(
        title = stringResource(Res.string.label_classes),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(working.classes, working.primaryClass) },
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            assigned.forEach { (clazz, level) ->
                ClassLevelRow(
                    clazz = clazz,
                    level = level,
                    maxLevel = working.maxLevelFor(clazz),
                    isPrimary = assigned.size >= 2 && clazz == working.primaryClass,
                    onLevelChange = { working = working.withClassLevel(clazz, it) },
                    onRemove = { working = working.withClassRemoved(clazz) },
                )
            }
            TextButton(
                onClick = { showPicker = true },
                enabled = working.canAddClass() && untakenExists,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(iconSizeMedium))
                Text(
                    text = stringResource(Res.string.btn_add_class),
                    modifier = Modifier.padding(start = spacingSmall),
                )
            }
        }
    }
}

@Composable
private fun ClassLevelRow(
    clazz: Character.Class,
    level: Int,
    maxLevel: Int,
    isPrimary: Boolean,
    onLevelChange: (Int) -> Unit,
    onRemove: () -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = spacingSmall)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth(),
        ) {
            ClassIcon(
                clazz = clazz,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(iconSizeMediumLarge),
            )
            Text(
                text = clazz.toFormattedString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            if (isPrimary) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(Res.string.label_primary_class),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(iconSizeMedium),
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(Res.string.btn_remove_class),
                )
            }
        }
        DecrementIncrementRow(
            value = level,
            minValue = MIN_CHARACTER_LEVEL,
            maxValue = maxLevel,
            onDecrement = { onLevelChange(level - 1) },
            onIncrement = { onLevelChange(level + 1) },
        )
    }
}

@Composable
private fun ClassPickerDialog(
    taken: Set<Character.Class>,
    onPick: (Character.Class) -> Unit,
    onDismiss: () -> Unit,
) {
    val options = Character.Class.entries - taken - Character.Class.UNKNOWN
    EditDialog(
        title = stringResource(Res.string.btn_add_class),
        onDismiss = onDismiss,
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            options.forEach { clazz ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPick(clazz) }
                        .padding(vertical = spacingMedium),
                ) {
                    ClassIcon(
                        clazz = clazz,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(iconSizeMediumLarge),
                    )
                    Text(text = clazz.toFormattedString(), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
