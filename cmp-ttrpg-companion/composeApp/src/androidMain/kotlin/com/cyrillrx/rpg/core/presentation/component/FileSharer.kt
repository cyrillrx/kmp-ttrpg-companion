package com.cyrillrx.rpg.core.presentation.component

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.cyrillrx.core.data.ExportFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

// Must match the provider authority and the cache path declared by the application manifest.
private const val FILE_PROVIDER_AUTHORITY_SUFFIX = ".fileprovider"
private const val EXPORT_DIRECTORY = "exports"

@Composable
actual fun rememberFileSharer(): FileSharer {
    val context = LocalContext.current
    return remember(context) { AndroidFileSharer(context) }
}

private class AndroidFileSharer(private val context: Context) : FileSharer {
    override suspend fun share(file: ExportFile) {
        val exported = withContext(Dispatchers.IO) {
            File(context.cacheDir, EXPORT_DIRECTORY)
                .apply { mkdirs() }
                .resolve(file.name)
                .apply { writeText(file.content) }
        }
        val uri = FileProvider.getUriForFile(context, context.packageName + FILE_PROVIDER_AUTHORITY_SUFFIX, exported)
        val send = Intent(Intent.ACTION_SEND).apply {
            type = file.mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            // The clip carries the read grant through the chooser to the app the user picks.
            clipData = ClipData.newRawUri(file.name, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(send, null))
    }
}
