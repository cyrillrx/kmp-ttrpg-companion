package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/** [content] is handed the way to close the menu, since picking an entry is what usually closes it. */
@Composable
fun IconMenu(
    icon: ImageVector,
    contentDescription: String,
    tint: Color = LocalContentColor.current,
    content: @Composable (dismiss: () -> Unit) -> Unit,
) {
    var showMenu: Boolean by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { showMenu = !showMenu }) {
            Icon(imageVector = icon, contentDescription = contentDescription, tint = tint)
        }
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            content { showMenu = false }
        }
    }
}
