package com.cyrillrx.rpg.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.presentation.theme.spacingMedium
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(router: HomeRouter) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingMedium),
    ) {
        HomeButton("Grimoire", router::openSpellBook)
        HomeButton("Bestiaire", router::openBestiary)
        HomeButton("Objets magiques", router::openInventory)

        HomeButton("Grimoire xml", router::openLegacySpellBook)
        HomeButton("Bestiaire xml", router::openLegacyBestiary)
        HomeButton("Objets magiques xml", router::openLegacyInventory)
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(object : HomeRouter {})
}

interface HomeRouter {
    fun openSpellBook() {}
    fun openBestiary() {}
    fun openInventory() {}

    fun openLegacySpellBook() {}
    fun openLegacyBestiary() {}
    fun openLegacyInventory() {}
}