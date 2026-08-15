import androidx.compose.ui.window.ComposeUIViewController
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.cache.IOSDatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.SharedDatabaseDriverFactory

private val databaseDriverFactory = SharedDatabaseDriverFactory(IOSDatabaseDriverFactory())

fun MainViewController() = ComposeUIViewController { App(databaseDriverFactory) }
