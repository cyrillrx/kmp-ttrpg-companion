package com.cyrillrx.rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { App() }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen(object : HomeRouter {})
    }
}
