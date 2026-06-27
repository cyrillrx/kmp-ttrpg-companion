package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
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
import com.cyrillrx.rpg.core.presentation.component.SwipeToDelete
import com.cyrillrx.rpg.core.presentation.component.rememberOptimisticDeleteHandler
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.snackbar_character_deleted
import rpg_companion.composeapp.generated.resources.snackbar_error_deleting_character
import rpg_companion.composeapp.generated.resources.title_character_list

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel,
    router: CharacterRouter,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.silentRefresh()
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.commitAllPendingDeletions() }
    }

    CharacterListScreen(
        state = state,
        events = viewModel.events,
        onNavigateUpClicked = router::navigateUp,
        onCharacterClicked = router::openCharacterDetail,
        onNewCharacterClicked = router::openCreateCharacter,
        onQuickCreateClicked = router::openPresetGallery,
        onDeleteCharacterOptimistically = viewModel::deleteCharacterOptimistically,
        onUndoDeletion = viewModel::undoDeletion,
        onCommitDeletion = viewModel::commitDeletion,
    )
}

@Composable
fun CharacterListScreen(
    state: CharacterListState,
    events: SharedFlow<CharacterListViewModel.Event>,
    onNavigateUpClicked: () -> Unit,
    onCharacterClicked: (Character) -> Unit,
    onNewCharacterClicked: () -> Unit,
    onQuickCreateClicked: () -> Unit,
    onDeleteCharacterOptimistically: (Character) -> CharacterListViewModel.PendingDeletion?,
    onUndoDeletion: (CharacterListViewModel.PendingDeletion) -> Unit,
    onCommitDeletion: (CharacterListViewModel.PendingDeletion) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is CharacterListViewModel.Event.DeletionError -> {
                    val errorMessage = getString(Res.string.snackbar_error_deleting_character, event.character.name)
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }

    val onDeleteCharacter = rememberOptimisticDeleteHandler(
        snackbarHostState = snackbarHostState,
        onDeleteOptimistically = onDeleteCharacterOptimistically,
        onUndo = onUndoDeletion,
        onCommit = onCommitDeletion,
        getMessage = { character -> getString(Res.string.snackbar_character_deleted, character.name) },
    )

    Scaffold(
        topBar = {
            SimpleTopBar(
                titleResource = Res.string.title_character_list,
                onNavigateUpClicked = onNavigateUpClicked,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
        ) {
            CharacterCreateActions(
                onNewCharacterClicked = onNewCharacterClicked,
                onQuickCreateClicked = onQuickCreateClicked,
                modifier = Modifier.padding(spacingMedium),
            )

            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (val body = state.body) {
                    is CharacterListState.Body.Loading -> Loader()
                    is CharacterListState.Body.Empty -> EmptySearch(searchQuery = state.searchQuery)
                    is CharacterListState.Body.Error -> ErrorLayout(errorMessage = body.errorMessage)
                    is CharacterListState.Body.WithData ->
                        CharacterList(
                            characters = body.searchResults,
                            onCharacterClicked = onCharacterClicked,
                            onDeleteCharacter = onDeleteCharacter,
                        )
                }
            }
        }
    }
}

@Composable
private fun CharacterList(
    characters: List<Character>,
    onCharacterClicked: (Character) -> Unit,
    onDeleteCharacter: (Character) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(characters, key = { it.id }) { character ->
            SwipeToDelete(
                onSwiped = { onDeleteCharacter(character) },
                modifier = Modifier.fillMaxWidth().animateItem(),
            ) {
                CharacterListItem(
                    character = character,
                    onClick = { onCharacterClicked(character) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
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
        events = MutableSharedFlow(),
        onNavigateUpClicked = {},
        onCharacterClicked = {},
        onNewCharacterClicked = {},
        onQuickCreateClicked = {},
        onDeleteCharacterOptimistically = { null },
        onUndoDeletion = {},
        onCommitDeletion = {},
    )
}
