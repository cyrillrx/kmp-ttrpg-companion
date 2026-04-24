package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.ui.Modifier

/**
 * Sets an accessibility ID that Maestro (and other UI testing tools) can target with `id: "<id>"`.
 *
 * On Android, this exposes the tag as a resource ID in the accessibility tree via
 * [testTagsAsResourceId]. On other platforms, it falls back to a plain testTag.
 *
 * Note: [androidx.compose.material3.AlertDialog] renders in a separate Android window, so this
 * must be applied inside the dialog - root-level semantics do not propagate across dialog
 * boundaries.
 */
expect fun Modifier.accessibilityId(id: String): Modifier
