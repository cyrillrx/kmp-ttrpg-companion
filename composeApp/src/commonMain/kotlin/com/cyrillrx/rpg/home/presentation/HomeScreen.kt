package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.common.theme.spacingMedium
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_alternative_spell_book
import rpg_companion.composeapp.generated.resources.btn_bestiary
import rpg_companion.composeapp.generated.resources.btn_inventory
import rpg_companion.composeapp.generated.resources.btn_legacy_bestiary
import rpg_companion.composeapp.generated.resources.btn_legacy_inventory
import rpg_companion.composeapp.generated.resources.btn_spell_book

@Composable
fun HomeScreen(router: HomeRouter) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingMedium),
    ) {
        HomeButton(stringResource(Res.string.btn_spell_book), router::openSpellBook)
        HomeButton(stringResource(Res.string.btn_alternative_spell_book), router::openAlternativeSpellBook)
        HomeButton(stringResource(Res.string.btn_bestiary), router::openBestiary)
        HomeButton(stringResource(Res.string.btn_inventory), router::openMagicalItems)

        HomeButton(stringResource(Res.string.btn_legacy_bestiary), router::openLegacyBestiary)
        HomeButton(stringResource(Res.string.btn_legacy_inventory), router::openLegacyInventory)
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(object : HomeRouter {})
}

interface HomeRouter {
    fun openSpellBook() {}
    fun openAlternativeSpellBook() {}
    fun openBestiary() {}

    fun openMagicalItems() {}
    fun openLegacyBestiary() {}
    fun openLegacyInventory() {}
}
