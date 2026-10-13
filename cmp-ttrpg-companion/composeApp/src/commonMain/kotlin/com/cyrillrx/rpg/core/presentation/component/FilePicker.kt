package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable

fun interface FilePicker {
    fun pick()

    companion object {
        const val MAX_FILE_BYTES = 1_048_576
    }
}

/**
 * Opens the platform file picker and reads the chosen file as UTF-8 text. Cancelling calls neither callback.
 * A file larger than [FilePicker.MAX_FILE_BYTES] fails the read rather than being loaded into memory.
 *
 * The result comes back through callbacks rather than a suspend call: on Android the picker is another
 * activity, and the screen may be recreated before it returns.
 *
 * TODO(#296): share the read pipeline across platforms and run it outside the composition.
 */
@Composable
expect fun rememberFilePicker(onFileRead: (content: String) -> Unit, onReadFailed: () -> Unit): FilePicker
