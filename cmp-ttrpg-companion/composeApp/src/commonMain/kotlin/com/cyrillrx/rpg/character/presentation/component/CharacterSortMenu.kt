package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.domain.CharacterSortOrder
import com.cyrillrx.rpg.core.presentation.component.IconMenu
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_sort
import rpg_companion.composeapp.generated.resources.label_sort_by
import rpg_companion.composeapp.generated.resources.sort_character_last_modified
import rpg_companion.composeapp.generated.resources.sort_character_name

private val sortOrderOptions = listOf(
    CharacterSortOrder.LAST_MODIFIED to Res.string.sort_character_last_modified,
    CharacterSortOrder.NAME to Res.string.sort_character_name,
)

@Composable
fun CharacterSortMenu(
    sortOrder: CharacterSortOrder,
    onSortOrderSelected: (CharacterSortOrder) -> Unit,
) {
    IconMenu(
        icon = Icons.AutoMirrored.Outlined.Sort,
        contentDescription = stringResource(Res.string.btn_sort),
        tint = if (sortOrder == CharacterSortOrder.LAST_MODIFIED) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.primary
        },
    ) { dismiss ->
        Text(
            text = stringResource(Res.string.label_sort_by),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = spacingCommon, vertical = spacingSmall),
        )
        sortOrderOptions.forEach { (order, label) ->
            DropdownMenuItem(
                text = { Text(text = stringResource(label)) },
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
