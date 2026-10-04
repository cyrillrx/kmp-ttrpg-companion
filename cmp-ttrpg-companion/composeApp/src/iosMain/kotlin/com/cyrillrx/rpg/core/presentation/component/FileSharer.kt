package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cyrillrx.core.data.ExportFile
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
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

        val presenter = topViewController() ?: return
        val controller = UIActivityViewController(activityItems = listOf(url), applicationActivities = null)
        // An iPad presents the sheet as a popover, which crashes without an anchor.
        controller.popoverPresentationController?.sourceView = presenter.view
        presenter.presentViewController(controller, animated = true, completion = null)
    }

    private fun topViewController(): UIViewController? {
        val root = UIApplication.sharedApplication.connectedScenes
            .filterIsInstance<UIWindowScene>()
            .flatMap { scene -> scene.windows.filterIsInstance<UIWindow>() }
            .firstOrNull { it.isKeyWindow() }
            ?.rootViewController
            ?: return null
        return generateSequence(root) { it.presentedViewController }.last()
    }
}
