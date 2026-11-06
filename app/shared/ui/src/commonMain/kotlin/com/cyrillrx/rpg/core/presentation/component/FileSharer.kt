package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import com.cyrillrx.core.data.ExportFile

fun interface FileSharer {
    suspend fun share(file: ExportFile)
}

/** Opens the share sheet on mobile and a save dialog on Desktop. */
@Composable
expect fun rememberFileSharer(): FileSharer
