package com.cyrillrx.rpg.userlist.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface EntityUiProvider<T> {
    fun getId(entity: T): String
    fun getDisplayName(entity: T): String

    @Composable
    fun Header(entity: T)

    @Composable
    fun ListItem(entity: T, modifier: Modifier)
}
