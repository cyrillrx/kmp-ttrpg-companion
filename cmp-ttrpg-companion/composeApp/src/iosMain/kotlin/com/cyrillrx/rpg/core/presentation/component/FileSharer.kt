package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cyrillrx.core.data.ExportFile
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetMidX
import platform.CoreGraphics.CGRectGetMidY
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.popoverPresentationController

@Composable
actual fun rememberFileSharer(): FileSharer = remember { IosFileSharer() }

private class IosFileSharer : FileSharer {
    @OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
    override suspend fun share(file: ExportFile) {
        val url = NSURL.fileURLWithPath(NSTemporaryDirectory() + file.name)
        val written = NSString.create(string = file.content)
            .writeToURL(url, atomically = true, encoding = NSUTF8StringEncoding, error = null)
        check(written) { "Unable to write ${file.name}" }

        val presenter = checkNotNull(topViewController()) { "No window to present the share sheet from" }
        val controller = UIActivityViewController(activityItems = listOf(url), applicationActivities = null)
        // An iPad presents the sheet as a popover, which crashes without an anchor.
        // Without a source rect it would point at the top-left corner, so it is centered with no arrow.
        controller.popoverPresentationController?.apply {
            val view = presenter.view
            sourceView = view
            sourceRect = CGRectMake(CGRectGetMidX(view.bounds), CGRectGetMidY(view.bounds), 0.0, 0.0)
            permittedArrowDirections = 0u
        }
        presenter.presentViewController(controller, animated = true, completion = null)
    }
}
