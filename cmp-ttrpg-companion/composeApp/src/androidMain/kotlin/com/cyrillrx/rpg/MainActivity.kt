package com.cyrillrx.rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.cache.AndroidDatabaseDriverFactory

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val databaseDriverFactory = AndroidDatabaseDriverFactory(applicationContext)
        setContent {
            // Expose Compose testTags as resource ids so UiAutomator (macrobenchmarks) can target
            // tagged nodes such as the compendium list containers.
            Box(Modifier.fillMaxSize().semantics { testTagsAsResourceId = true }) {
                App(databaseDriverFactory)
            }
        }
    }
}
