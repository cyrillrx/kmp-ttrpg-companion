package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_more

@Composable
fun OverflowMenu(content: @Composable (dismiss: () -> Unit) -> Unit) {
    IconMenu(
        icon = Icons.Outlined.MoreVert,
        contentDescription = stringResource(Res.string.btn_more),
        content = content,
    )
}
