import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.cache.DesktopDatabaseDriverFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TTRPG companion",
    ) {
        val databaseDriverFactory = DesktopDatabaseDriverFactory()
        App(databaseDriverFactory)
    }
}
