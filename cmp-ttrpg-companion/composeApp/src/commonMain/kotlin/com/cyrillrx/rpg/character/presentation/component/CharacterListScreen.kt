package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_new_character
import rpg_companion.composeapp.generated.resources.btn_new_character_subtitle
import rpg_companion.composeapp.generated.resources.btn_quick_create
import rpg_companion.composeapp.generated.resources.btn_quick_create_subtitle
import rpg_companion.composeapp.generated.resources.btn_undo
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

    CharacterListScreen(
        state = state,
        events = viewModel.events,
        onNavigateUpClicked = router::navigateUp,
        onCharacterClicked = viewModel::openCharacterDetail,
        onNewCharacterClicked = viewModel::openCreateCharacter,
        onQuickCreateClicked = viewModel::openPresetGallery,
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
    val coroutineScope = rememberCoroutineScope()
    val undoLabel = stringResource(Res.string.btn_undo)

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

    fun onDeleteCharacter(character: Character) {
        val pending = onDeleteCharacterOptimistically(character) ?: return

        coroutineScope.launch {
            val deletedMessage = getString(Res.string.snackbar_character_deleted, character.name)
            val result = snackbarHostState.showSnackbar(
                message = deletedMessage,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> onUndoDeletion(pending)
                SnackbarResult.Dismissed -> onCommitDeletion(pending)
            }
        }
    }

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
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(spacingCommon),
            ) {
                CharacterActionCard(
                    icon = Icons.Filled.Add,
                    title = stringResource(Res.string.btn_new_character),
                    subtitle = stringResource(Res.string.btn_new_character_subtitle),
                    onClick = onNewCharacterClicked,
                    filled = true,
                    modifier = Modifier.weight(1f),
                )
                CharacterActionCard(
                    icon = Icons.Filled.Bolt,
                    title = stringResource(Res.string.btn_quick_create),
                    subtitle = stringResource(Res.string.btn_quick_create_subtitle),
                    onClick = onQuickCreateClicked,
                    filled = false,
                    modifier = Modifier.weight(1f),
                )
            }

            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (val body = state.body) {
                    is CharacterListState.Body.Loading -> Loader()
                    is CharacterListState.Body.Empty -> EmptySearch(searchQuery = state.searchQuery)
                    is CharacterListState.Body.Error -> ErrorLayout(errorMessage = body.errorMessage)
                    is CharacterListState.Body.WithData ->
                        CharacterList(
                            characters = body.searchResults,
                            onCharacterClicked = onCharacterClicked,
                            onDeleteCharacter = ::onDeleteCharacter,
                        )
                }
            }
        }
    }
}

@Composable
private fun CharacterActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    filled: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (filled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (filled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = if (filled) null else BorderStroke(borderWidth, MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha)),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            modifier = Modifier.padding(spacingCommon),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor,
            )
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
