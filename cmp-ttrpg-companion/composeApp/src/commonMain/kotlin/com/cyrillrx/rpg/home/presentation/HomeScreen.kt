package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.contentMaxWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.widthExpandedMin
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.app_name
import rpg_companion.composeapp.generated.resources.btn_bestiary
import rpg_companion.composeapp.generated.resources.btn_campaign_list
import rpg_companion.composeapp.generated.resources.btn_character_sheets
import rpg_companion.composeapp.generated.resources.btn_magical_items
import rpg_companion.composeapp.generated.resources.btn_my_bestiary_lists
import rpg_companion.composeapp.generated.resources.btn_my_item_lists
import rpg_companion.composeapp.generated.resources.btn_my_spell_lists
import rpg_companion.composeapp.generated.resources.btn_spell_book
import rpg_companion.composeapp.generated.resources.section_compendium
import rpg_companion.composeapp.generated.resources.section_my_lists
import rpg_companion.composeapp.generated.resources.title_settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(router: HomeRouter) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                actions = {
                    IconButton(onClick = router::openSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = stringResource(Res.string.title_settings))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            val contentModifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = contentMaxWidth)
                .fillMaxWidth()

            if (maxWidth >= widthExpandedMin) {
                HomeWideScreen(router, contentModifier)
            } else {
                HomeDefault(router, contentModifier)
            }
        }
    }
}

/** Single-column layout for phones and narrow tablets. */
@Composable
private fun HomeDefault(router: HomeRouter, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacingCommon, vertical = spacingMedium),
    ) {
        HomeButton(stringResource(Res.string.btn_campaign_list), router::openCampaignList)
        HomeButton(stringResource(Res.string.btn_character_sheets), router::openCharacterSheetList)
        CompendiumSection(router, modifier = Modifier.fillMaxWidth())
        MyListsSection(router, modifier = Modifier.fillMaxWidth())
    }
}

/** Two-pane layout for large landscape screens. */
@Composable
private fun HomeWideScreen(router: HomeRouter, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacingCommon, vertical = spacingMedium),
    ) {
        HomeButton(stringResource(Res.string.btn_campaign_list), router::openCampaignList)
        HomeButton(stringResource(Res.string.btn_character_sheets), router::openCharacterSheetList)
        Row(horizontalArrangement = Arrangement.spacedBy(spacingCommon)) {
            CompendiumSection(router, modifier = Modifier.weight(1f))
            MyListsSection(router, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun CompendiumSection(router: HomeRouter, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = modifier,
    ) {
        SectionHeader(title = stringResource(Res.string.section_compendium))
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingCommon),
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconLabelButton(
                label = stringResource(Res.string.btn_spell_book),
                icon = Icons.Filled.AutoAwesome,
                onClick = router::openSpellCompendium,
                modifier = Modifier.weight(1f),
            )
            IconLabelButton(
                label = stringResource(Res.string.btn_magical_items),
                icon = Icons.Filled.Diamond,
                onClick = router::openMagicalItemCompendium,
                modifier = Modifier.weight(1f),
            )
            IconLabelButton(
                label = stringResource(Res.string.btn_bestiary),
                icon = Icons.Filled.Pets,
                onClick = router::openMonsterCompendium,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun MyListsSection(router: HomeRouter, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = modifier,
    ) {
        SectionHeader(title = stringResource(Res.string.section_my_lists))
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingCommon),
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconLabelButton(
                label = stringResource(Res.string.btn_my_spell_lists),
                icon = Icons.Filled.AutoAwesome,
                onClick = router::openMySpellLists,
                modifier = Modifier.weight(1f),
            )
            IconLabelButton(
                label = stringResource(Res.string.btn_my_item_lists),
                icon = Icons.Filled.Diamond,
                onClick = router::openMyMagicalItemLists,
                modifier = Modifier.weight(1f),
            )
            IconLabelButton(
                label = stringResource(Res.string.btn_my_bestiary_lists),
                icon = Icons.Filled.Pets,
                onClick = router::openMyMonsterLists,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private object PreviewHomeRouter : HomeRouter {
    override fun openCampaignList() {}
    override fun openCharacterSheetList() {}
    override fun openSpellCompendium() {}
    override fun openMagicalItemCompendium() {}
    override fun openMonsterCompendium() {}
    override fun openMySpellLists() {}
    override fun openMyMagicalItemLists() {}
    override fun openMyMonsterLists() {}
    override fun openSettings() {}
}

@Preview
@Composable
private fun PreviewHomeScreenLight() {
    AppThemePreview(darkTheme = false) {
        HomeScreen(PreviewHomeRouter)
    }
}

@Preview
@Composable
private fun PreviewHomeScreenDark() {
    AppThemePreview(darkTheme = true) {
        HomeScreen(PreviewHomeRouter)
    }
}
