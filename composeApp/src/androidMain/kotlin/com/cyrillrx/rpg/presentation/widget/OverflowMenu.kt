package com.cyrillrx.rpg.presentation.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.cyrillrx.rpg.R

@Composable
fun OverflowMenu(content: @Composable () -> Unit) {
    var showMenu: Boolean by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { showMenu = !showMenu }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = stringResource(R.string.btn_more),
            )
        }
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) { content() }
    }
}
