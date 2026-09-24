package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.domain.CharacterSortOrder
import com.cyrillrx.rpg.core.presentation.component.AnchoredMenu
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
    AnchoredMenu(
        anchor = { toggle ->
            IconButton(onClick = toggle) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Sort,
                    contentDescription = stringResource(Res.string.btn_sort),
                )
            }
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
