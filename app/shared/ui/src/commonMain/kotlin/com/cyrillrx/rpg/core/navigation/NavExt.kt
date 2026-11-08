package com.cyrillrx.rpg.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

// TODO(#401): make navigation actions idempotent per entry
fun NavBackStack<NavKey>.navigateUp() {
    if (size > 1) {
        removeLastOrNull()
    }
}
