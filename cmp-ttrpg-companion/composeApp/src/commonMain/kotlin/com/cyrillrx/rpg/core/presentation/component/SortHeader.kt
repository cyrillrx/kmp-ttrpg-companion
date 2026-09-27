package com.cyrillrx.rpg.core.presentation.component

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
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_sort
import rpg_companion.composeapp.generated.resources.label_sort_by

/** Sits at the head of a list, spelling out the order in force rather than hiding it behind an icon. */
@Composable
fun <T> SortHeader(
    selected: T,
    options: List<Pair<T, StringResource>>,
    onSelected: (T) -> Unit,
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
                    Text(text = stringResource(options.first { it.first == selected }.second))
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
            options.forEach { (option, label) ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(label)) },
                    onClick = {
                        onSelected(option)
                        dismiss()
                    },
                    trailingIcon = {
                        if (option == selected) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    },
                )
            }
        }
    }
}
