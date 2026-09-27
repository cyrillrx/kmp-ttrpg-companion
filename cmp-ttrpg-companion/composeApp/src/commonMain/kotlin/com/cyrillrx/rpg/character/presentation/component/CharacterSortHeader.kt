package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.domain.StoredSortOrder
import com.cyrillrx.rpg.core.presentation.component.AnchoredMenu
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_sort
import rpg_companion.composeapp.generated.resources.label_sort_by
import rpg_companion.composeapp.generated.resources.sort_character_last_modified
import rpg_companion.composeapp.generated.resources.sort_character_name

@Composable
fun CharacterSortHeader(
    sortOrder: StoredSortOrder,
    onSortOrderSelected: (StoredSortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier.fillMaxWidth(),
    ) {
        AnchoredMenu(
            anchor = { toggle ->
                TextButton(onClick = toggle) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Sort,
                        contentDescription = stringResource(Res.string.btn_sort),
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(sortOrder.toStringRes()))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
        ) { dismiss ->
            Text(
                text = stringResource(Res.string.label_sort_by),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = spacingCommon, vertical = spacingSmall),
            )
            StoredSortOrder.entries.forEach { order ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(order.toStringRes())) },
                    onClick = {
                        onSortOrderSelected(order)
                        dismiss()
                    },
                    trailingIcon = {
                        if (order == sortOrder) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    },
                )
            }
        }
    }
}

private fun StoredSortOrder.toStringRes(): StringResource = when (this) {
    StoredSortOrder.LAST_MODIFIED -> Res.string.sort_character_last_modified
    StoredSortOrder.NAME -> Res.string.sort_character_name
}

@Preview
@Composable
private fun PreviewCharacterSortHeaderLight() {
    AppThemePreview(darkTheme = false) {
        CharacterSortHeader(sortOrder = StoredSortOrder.LAST_MODIFIED, onSortOrderSelected = {})
    }
}

@Preview
@Composable
private fun PreviewCharacterSortHeaderDark() {
    AppThemePreview(darkTheme = true) {
        CharacterSortHeader(sortOrder = StoredSortOrder.NAME, onSortOrderSelected = {})
    }
}
