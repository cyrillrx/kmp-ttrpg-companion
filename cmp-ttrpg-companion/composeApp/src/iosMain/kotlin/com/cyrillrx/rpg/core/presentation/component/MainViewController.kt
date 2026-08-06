import androidx.compose.ui.window.ComposeUIViewController
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.cache.IOSDatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.SharedDatabaseDriverFactory

/**
 * Process-wide so that neither a recomposition nor a second view controller opens another
 * connection to the same database file.
 */
private val databaseDriverFactory = SharedDatabaseDriverFactory(IOSDatabaseDriverFactory())

fun MainViewController() = ComposeUIViewController { App(databaseDriverFactory) }
