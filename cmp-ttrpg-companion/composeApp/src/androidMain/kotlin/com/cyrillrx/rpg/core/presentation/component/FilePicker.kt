package com.cyrillrx.rpg.core.presentation.component

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

private val PICKABLE_MIME_TYPES = arrayOf("*/*")

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
    return remember(launcher) {
        FilePicker {
            try {
                launcher.launch(PICKABLE_MIME_TYPES)
            } catch (_: ActivityNotFoundException) {
                currentOnReadFailed.value()
            }
        }
    }
}

private fun Context.readText(uri: Uri): String {
    val stream = checkNotNull(contentResolver.openInputStream(uri)) { "Unable to open $uri" }
    val bytes = stream.use { it.readAtMost(FilePicker.MAX_FILE_BYTES + 1) }
    check(bytes.size <= FilePicker.MAX_FILE_BYTES) { "$uri exceeds ${FilePicker.MAX_FILE_BYTES} bytes" }
    return bytes.decodeToString(throwOnInvalidSequence = true)
}

private fun InputStream.readAtMost(limit: Int): ByteArray {
    val buffer = ByteArray(limit)
    var total = 0
    while (total < limit) {
        val read = read(buffer, total, limit - total)
        if (read == -1) break
        total += read
    }
    return buffer.copyOf(total)
}
