package com.cyrillrx.rpg.core.presentation.component

import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

internal fun topViewController(): UIViewController? {
    val root = UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { scene -> scene.windows.filterIsInstance<UIWindow>() }
        .firstOrNull { it.isKeyWindow() }
        ?.rootViewController
        ?: return null
    return generateSequence(root) { it.presentedViewController }.last()
}
