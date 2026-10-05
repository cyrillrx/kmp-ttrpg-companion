package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeData
import platform.UniformTypeIdentifiers.UTTypeJSON
import platform.UniformTypeIdentifiers.UTTypePlainText
import platform.darwin.NSObject

private val PICKABLE_CONTENT_TYPES = listOf(UTTypeJSON, UTTypeData, UTTypePlainText)

@Composable
actual fun rememberFilePicker(onFileRead: (content: String) -> Unit, onReadFailed: () -> Unit): FilePicker {
    val currentOnFileRead = rememberUpdatedState(onFileRead)
    val currentOnReadFailed = rememberUpdatedState(onReadFailed)
    // UIKit holds its delegate weakly: the composition keeps it alive while the picker is shown.
    val delegate = remember {
        DocumentPickerDelegate { url ->
            val content = url.readText()
            if (content == null) currentOnReadFailed.value() else currentOnFileRead.value(content)
        }
    }
    return remember(delegate) {
        FilePicker {
            val presenter = topViewController() ?: return@FilePicker currentOnReadFailed.value()
            val picker = UIDocumentPickerViewController(forOpeningContentTypes = PICKABLE_CONTENT_TYPES, asCopy = true)
            picker.delegate = delegate
            presenter.presentViewController(picker, animated = true, completion = null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSURL.readText(): String? {
    val attributes = path?.let { NSFileManager.defaultManager.attributesOfItemAtPath(it, null) } ?: return null
    val size = (attributes[NSFileSize] as? NSNumber)?.longLongValue ?: return null
    if (size > FilePicker.MAX_FILE_BYTES) return null
    return NSString.stringWithContentsOfURL(this, NSUTF8StringEncoding, null)
}

private class DocumentPickerDelegate(private val onPicked: (NSURL) -> Unit) :
    NSObject(),
    UIDocumentPickerDelegateProtocol {
    override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>) {
        (didPickDocumentsAtURLs.firstOrNull() as? NSURL)?.let(onPicked)
    }
}
