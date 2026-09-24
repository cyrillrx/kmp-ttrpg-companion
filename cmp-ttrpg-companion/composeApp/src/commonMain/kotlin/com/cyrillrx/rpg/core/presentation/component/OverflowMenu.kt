package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_more

@Composable
fun OverflowMenu(content: @Composable (dismiss: () -> Unit) -> Unit) {
    AnchoredMenu(
        anchor = { toggle ->
            IconButton(onClick = toggle) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = stringResource(Res.string.btn_more),
                )
            }
        },
        content = content,
    )
}
