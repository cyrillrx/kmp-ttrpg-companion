package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AnchoredMenu(
    anchor: @Composable (toggle: () -> Unit) -> Unit,
    content: @Composable (dismiss: () -> Unit) -> Unit,
) {
    var showMenu: Boolean by remember { mutableStateOf(false) }

    Box {
        anchor { showMenu = !showMenu }
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            content { showMenu = false }
        }
    }
}
