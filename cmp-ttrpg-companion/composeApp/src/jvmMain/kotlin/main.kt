import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.cache.DesktopDatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.SharedDatabaseDriverFactory

fun main() {
    // Built outside the composition: a recomposition would otherwise open a second connection.
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
        // After application { } returns, so the composition is disposed and no query is still in
        // flight when the connection goes away.
        databaseDriverFactory.close()
    }
}
