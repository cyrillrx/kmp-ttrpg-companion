package com.cyrillrx.rpg.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateUp() {
    if (size > 1) removeAt(index = size - 1)
}
