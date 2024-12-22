package com.cyrillrx.rpg.dnd.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(object : HomeRouter {})
}
