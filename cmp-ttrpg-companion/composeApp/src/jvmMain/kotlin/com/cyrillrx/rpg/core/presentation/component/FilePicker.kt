package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_import
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberFilePicker(onFileRead: (content: String) -> Unit, onReadFailed: () -> Unit): FilePicker {
    val dialogTitle = stringResource(Res.string.btn_import)
    val scope = rememberCoroutineScope()
    val currentOnFileRead = rememberUpdatedState(onFileRead)
    val currentOnReadFailed = rememberUpdatedState(onReadFailed)
    return FilePicker {
        val file = chooseFile(dialogTitle) ?: return@FilePicker
        scope.launch {
            val content = try {
                withContext(Dispatchers.IO) {
                    check(file.length() <= FilePicker.MAX_FILE_BYTES) {
                        "$file exceeds ${FilePicker.MAX_FILE_BYTES} bytes"
                    }
                    file.readBytes().decodeToString(throwOnInvalidSequence = true)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                null
            }
            if (content == null) currentOnReadFailed.value() else currentOnFileRead.value(content)
        }
    }
}

private fun chooseFile(dialogTitle: String): File? {
    val dialog = FileDialog(null as Frame?, dialogTitle, FileDialog.LOAD)
    return try {
        dialog.isVisible = true
        File(dialog.directory ?: return null, dialog.file ?: return null)
    } finally {
        dialog.dispose()
    }
}
