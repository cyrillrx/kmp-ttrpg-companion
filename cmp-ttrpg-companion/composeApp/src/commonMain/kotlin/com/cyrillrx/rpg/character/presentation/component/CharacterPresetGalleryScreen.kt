package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.presentation.CharacterPresetGalleryState
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterPresetGalleryViewModel
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.tab_non_player_characters
import rpg_companion.composeapp.generated.resources.tab_player_characters
import rpg_companion.composeapp.generated.resources.title_preset_gallery

@Composable
fun CharacterPresetGalleryScreen(
    viewModel: CharacterPresetGalleryViewModel,
    router: CharacterRouter,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterPresetGalleryScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onTabSelected = viewModel::onTabSelected,
        onPresetSelected = viewModel::onPresetSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterPresetGalleryScreen(
    state: CharacterPresetGalleryState,
    onNavigateUpClicked: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onPresetSelected: (Character) -> Unit,
) {
    Scaffold(
        topBar = {
            Column {
                SimpleTopBar(
                    titleResource = Res.string.title_preset_gallery,
                    onNavigateUpClicked = onNavigateUpClicked,
                )
                if (state.body is CharacterPresetGalleryState.Body.WithData) {
                    PrimaryTabRow(selectedTabIndex = state.selectedTabIndex) {
                        Tab(
                            selected = state.selectedTabIndex == 0,
                            onClick = { onTabSelected(0) },
                            text = { Text(stringResource(Res.string.tab_player_characters)) },
                        )
                        Tab(
                            selected = state.selectedTabIndex == 1,
                            onClick = { onTabSelected(1) },
                            text = { Text(stringResource(Res.string.tab_non_player_characters)) },
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        when (val body = state.body) {
            is CharacterPresetGalleryState.Body.Loading -> Loader()
            is CharacterPresetGalleryState.Body.Empty -> EmptySearch(searchQuery = "")
            is CharacterPresetGalleryState.Body.Error -> ErrorLayout(errorMessage = body.errorMessage)
            is CharacterPresetGalleryState.Body.WithData -> {
                val presets = if (state.selectedTabIndex == 0) body.pcPresets else body.npcPresets
                PresetList(
                    presets = presets,
                    onPresetSelected = onPresetSelected,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}

@Composable
private fun PresetList(
    presets: List<Character>,
    onPresetSelected: (Character) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(presets, key = { it.id }) { preset ->
            CharacterListItem(
                character = preset,
                onClick = { onPresetSelected(preset) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterPresetGalleryScreenLight() {
    AppThemePreview(darkTheme = false) { CharacterPresetGalleryScreenPreview() }
}

@Preview
@Composable
private fun PreviewCharacterPresetGalleryScreenDark() {
    AppThemePreview(darkTheme = true) { CharacterPresetGalleryScreenPreview() }
}

@Composable
private fun CharacterPresetGalleryScreenPreview() {
    val characters = SampleCharacterRepository.getAll()
    CharacterPresetGalleryScreen(
        state = CharacterPresetGalleryState(
            selectedTabIndex = 0,
            body = CharacterPresetGalleryState.Body.WithData(
                pcPresets = characters,
                npcPresets = emptyList(),
            ),
        ),
        onNavigateUpClicked = {},
        onTabSelected = {},
        onPresetSelected = {},
    )
}
