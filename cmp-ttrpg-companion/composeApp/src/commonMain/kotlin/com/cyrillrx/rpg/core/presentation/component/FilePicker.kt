package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable

fun interface FilePicker {
    fun pick()
}

/**
 * Opens the platform file picker and reads the chosen file as UTF-8 text. Cancelling calls neither callback.
 *
 * The result comes back through callbacks rather than a suspend call: on Android the picker is another
 * activity, and the screen may be recreated before it returns.
 */
@Composable
expect fun rememberFilePicker(onFileRead: (content: String) -> Unit, onReadFailed: () -> Unit): FilePicker
