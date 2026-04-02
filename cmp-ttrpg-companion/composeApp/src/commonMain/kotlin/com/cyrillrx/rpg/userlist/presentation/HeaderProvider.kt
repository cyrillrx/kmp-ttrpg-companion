package com.cyrillrx.rpg.userlist.presentation

import androidx.compose.runtime.Composable

interface HeaderProvider<T> {
    @Composable
    fun Header(entity: T)
}
