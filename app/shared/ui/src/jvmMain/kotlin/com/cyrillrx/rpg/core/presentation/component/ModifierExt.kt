package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

actual fun Modifier.accessibilityId(id: String): Modifier = testTag(id)
