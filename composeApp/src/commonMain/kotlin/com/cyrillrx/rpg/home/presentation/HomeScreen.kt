package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_alternative_spell_book
import rpg_companion.composeapp.generated.resources.btn_bestiary
import rpg_companion.composeapp.generated.resources.btn_character_sheets
import rpg_companion.composeapp.generated.resources.btn_inventory
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
        HomeButton(stringResource(Res.string.btn_character_sheets), router::openCharacterSheets)
    }
}

interface HomeRouter {
    fun openSpellBook() {}
    fun openAlternativeSpellBook() {}
    fun openMagicalItems() {}
    fun openBestiary() {}
    fun openCharacterSheets() {}
}
