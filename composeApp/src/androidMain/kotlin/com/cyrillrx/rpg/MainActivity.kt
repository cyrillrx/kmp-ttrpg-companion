package com.cyrillrx.rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.core.data.cache.AndroidDatabaseDriverFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val databaseDriverFactory = AndroidDatabaseDriverFactory(applicationContext)
        setContent { App(databaseDriverFactory) }
    }
}
