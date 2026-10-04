package com.cyrillrx.rpg.core.presentation.component

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Messaging and mail apps often save an attachment without its JSON type.
private val PICKABLE_MIME_TYPES = arrayOf("application/json", "application/octet-stream", "text/plain")

@Composable
actual fun rememberFilePicker(onFileRead: (content: String) -> Unit, onReadFailed: () -> Unit): FilePicker {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentOnFileRead = rememberUpdatedState(onFileRead)
    val currentOnReadFailed = rememberUpdatedState(onReadFailed)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val content = try {
                withContext(Dispatchers.IO) { context.readText(uri) }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                null
            }
            if (content == null) currentOnReadFailed.value() else currentOnFileRead.value(content)
        }
    }
    return FilePicker { launcher.launch(PICKABLE_MIME_TYPES) }
}

private fun Context.readText(uri: Uri): String =
    checkNotNull(contentResolver.openInputStream(uri)) { "Unable to open $uri" }
        .bufferedReader()
        .use { it.readText() }
