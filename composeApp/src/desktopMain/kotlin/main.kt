import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cyrillrx.rpg.app.App

fun main() = application {
//    Logger.addChild(SystemOutLog(Severity.VERBOSE, clickableLogs = false))

    Window(
        onCloseRequest = ::exitApplication,
        title = "TTRPG companion",
    ) {
        App()
    }
}
