package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingLarge
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.app_name
import rpg_companion.composeapp.generated.resources.btn_alternative_spell_book
import rpg_companion.composeapp.generated.resources.btn_bestiary
import rpg_companion.composeapp.generated.resources.btn_campaign_list
import rpg_companion.composeapp.generated.resources.btn_character_sheets
import rpg_companion.composeapp.generated.resources.btn_inventory
import rpg_companion.composeapp.generated.resources.btn_spell_book

@Composable
fun HomeScreen(router: HomeRouter) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(spacingLarge)
                    .align(alignment = Alignment.CenterHorizontally),
            )

            HomeButton(stringResource(Res.string.btn_campaign_list), router::openCampaignList)
            HomeButton(stringResource(Res.string.btn_character_sheets), router::openCharacterSheets)

            HorizontalDivider()

            HomeButton(stringResource(Res.string.btn_spell_book), router::openSpellBook)
            HomeButton(stringResource(Res.string.btn_alternative_spell_book), router::openAlternativeSpellBook)
            HomeButton(stringResource(Res.string.btn_bestiary), router::openBestiary)
            HomeButton(stringResource(Res.string.btn_inventory), router::openMagicalItems)
        }
    }
}

@Preview
@Composable
private fun PreviewHomeScreenLight() {
    AppThemePreview(darkTheme = false) {
        HomeScreen(object : HomeRouter {})
    }
}

@Preview
@Composable
private fun PreviewHomeScreenDark() {
    AppThemePreview(darkTheme = true) {
        HomeScreen(object : HomeRouter {})
    }
}
