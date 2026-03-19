package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingLarge
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.app_name
import rpg_companion.composeapp.generated.resources.btn_bestiary
import rpg_companion.composeapp.generated.resources.btn_campaign_list
import rpg_companion.composeapp.generated.resources.btn_character_sheets
import rpg_companion.composeapp.generated.resources.btn_inventory
import rpg_companion.composeapp.generated.resources.btn_spell_book
import rpg_companion.composeapp.generated.resources.section_compendium

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

            SectionHeader(
                title = stringResource(Res.string.section_compendium),
                modifier = Modifier.padding(spacingCommon),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingCommon),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingCommon),
            ) {
                CompendiumButton(
                    label = stringResource(Res.string.btn_spell_book),
                    icon = Icons.Filled.AutoAwesome,
                    onClick = router::openSpellBook,
                    modifier = Modifier.weight(1f),
                )
                CompendiumButton(
                    label = stringResource(Res.string.btn_inventory),
                    icon = Icons.Filled.Diamond,
                    onClick = router::openMagicalItems,
                    modifier = Modifier.weight(1f),
                )
                CompendiumButton(
                    label = stringResource(Res.string.btn_bestiary),
                    icon = Icons.Filled.Pets,
                    onClick = router::openBestiary,
                    modifier = Modifier.weight(1f),
                )
            }
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
