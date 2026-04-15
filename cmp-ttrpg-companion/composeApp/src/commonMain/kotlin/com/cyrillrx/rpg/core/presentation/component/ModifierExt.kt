package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId

/**
 * Sets an accessibility ID that Maestro (and other UI testing tools) can target with `id: "<id>"`.
 *
 * [Modifier.testTag] only exposes the tag to the Compose semantics testing framework.
 * Setting [testTagsAsResourceId] = true makes it visible in the Android accessibility tree as a resource ID,
 * which is what Maestro's `id:` selector queries.
 *
 * Note: [androidx.compose.material3.AlertDialog] renders in a separate Android window, so this must be applied inside the dialog.
 * root-level semantics do not propagate across dialog boundaries.
 */
fun Modifier.accessibilityId(id: String): Modifier = semantics {
    testTag = id
    testTagsAsResourceId = true
}
