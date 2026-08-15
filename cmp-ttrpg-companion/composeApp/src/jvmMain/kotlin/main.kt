import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.cache.DesktopDatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.SharedDatabaseDriverFactory

fun main() {
    val databaseDriverFactory = SharedDatabaseDriverFactory(DesktopDatabaseDriverFactory())

    try {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "TTRPG companion",
            ) {
                App(databaseDriverFactory)
            }
        }
    } finally {
        databaseDriverFactory.close()
    }
}
