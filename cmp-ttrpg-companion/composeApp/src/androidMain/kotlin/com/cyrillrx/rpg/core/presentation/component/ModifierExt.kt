package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId

actual fun Modifier.accessibilityId(id: String): Modifier = semantics {
    testTag = id
    testTagsAsResourceId = true
}
