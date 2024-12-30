package com.cyrillrx.rpg.dnd.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouter

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(object : HomeRouter {})
}
