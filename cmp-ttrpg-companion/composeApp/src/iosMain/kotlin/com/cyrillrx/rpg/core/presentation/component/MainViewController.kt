import androidx.compose.ui.window.ComposeUIViewController
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.cache.IOSDatabaseDriverFactory

fun MainViewController() = ComposeUIViewController() {
    val databaseDriverFactory = IOSDatabaseDriverFactory()
    App(databaseDriverFactory)
}
