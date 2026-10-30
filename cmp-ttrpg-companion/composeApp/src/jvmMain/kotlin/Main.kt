import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.applicationDataDirectory
import com.cyrillrx.rpg.core.data.cache.DesktopDatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.SharedDatabaseDriverFactory

private const val APP_NAME = "TTRPG Companion"

fun main() {
    val dataDirectory = applicationDataDirectory(APP_NAME)
    val databaseDriverFactory = SharedDatabaseDriverFactory(DesktopDatabaseDriverFactory(dataDirectory))

    try {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = APP_NAME,
            ) {
                App(databaseDriverFactory)
            }
        }
    } finally {
        databaseDriverFactory.close()
    }
}
