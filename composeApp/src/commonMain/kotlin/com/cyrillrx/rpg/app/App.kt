package com.cyrillrx.rpg.app

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.presentation.home.HomeRouter
import com.cyrillrx.rpg.presentation.home.HomeScreen
import com.cyrillrx.rpg.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        HomeScreen(object : HomeRouter {})
    }
}
