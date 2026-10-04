package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cyrillrx.core.data.ExportFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_export
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberFileSharer(): FileSharer {
    val dialogTitle = stringResource(Res.string.btn_export)
    return remember(dialogTitle) { DesktopFileSharer(dialogTitle) }
}

private class DesktopFileSharer(private val dialogTitle: String) : FileSharer {
    override suspend fun share(file: ExportFile) {
        val dialog = FileDialog(null as Frame?, dialogTitle, FileDialog.SAVE).apply { this.file = file.name }
        val target =
            try {
                dialog.isVisible = true
                File(dialog.directory ?: return, dialog.file ?: return)
            } finally {
                dialog.dispose()
            }
        withContext(Dispatchers.IO) { target.writeText(file.content) }
    }
}
