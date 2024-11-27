import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cyrillrx.rpg.app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RPG companion",
    ) {
        App()
    }
}
