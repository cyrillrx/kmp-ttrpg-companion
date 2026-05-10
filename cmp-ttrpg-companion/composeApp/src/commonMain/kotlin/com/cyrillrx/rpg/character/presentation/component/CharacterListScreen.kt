package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.presentation.CharacterListState
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterListViewModel
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_new_character
import rpg_companion.composeapp.generated.resources.btn_quick_create
import rpg_companion.composeapp.generated.resources.title_character_list

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel,
    router: CharacterRouter,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterListScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onCharacterClicked = viewModel::onCharacterClicked,
        onNewCharacterClicked = viewModel::onCreateCharacterClicked,
        onQuickCreateClicked = viewModel::onQuickCreateClicked,
    )
}

@Composable
fun CharacterListScreen(
    state: CharacterListState,
    onNavigateUpClicked: () -> Unit,
    onCharacterClicked: (Character) -> Unit,
    onNewCharacterClicked: () -> Unit,
    onQuickCreateClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                titleResource = Res.string.title_character_list,
                onNavigateUpClicked = onNavigateUpClicked,
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(spacingCommon),
            ) {
                Button(
                    onClick = onNewCharacterClicked,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(Res.string.btn_new_character))
                }
                Button(
                    onClick = onQuickCreateClicked,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(Res.string.btn_quick_create))
                }
            }
        },
    ) { paddingValues ->
        when (val body = state.body) {
            is CharacterListState.Body.Loading -> Loader()
            is CharacterListState.Body.Empty -> EmptySearch(searchQuery = state.searchQuery)
            is CharacterListState.Body.Error -> ErrorLayout(errorMessage = body.errorMessage)
            is CharacterListState.Body.WithData ->
                CharacterList(
                    characters = body.searchResults,
                    onCharacterClicked = onCharacterClicked,
                    modifier = Modifier.padding(paddingValues),
                )
        }
    }
}

@Composable
private fun CharacterList(
    characters: List<Character>,
    onCharacterClicked: (Character) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(characters, key = { it.id }) { character ->
            CharacterListItem(
                character = character,
                onClick = { onCharacterClicked(character) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterListScreenLight() {
    AppThemePreview(darkTheme = false) { CharacterListScreenPreview() }
}

@Preview
@Composable
private fun PreviewCharacterListScreenDark() {
    AppThemePreview(darkTheme = true) { CharacterListScreenPreview() }
}

@Composable
private fun CharacterListScreenPreview() {
    CharacterListScreen(
        state =
            CharacterListState(
                searchQuery = "",
                body = CharacterListState.Body.WithData(SampleCharacterRepository.getAll()),
            ),
        onNavigateUpClicked = {},
        onCharacterClicked = {},
        onNewCharacterClicked = {},
        onQuickCreateClicked = {},
    )
}
