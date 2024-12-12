package com.cyrillrx.rpg.spellbook.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.SearchBar
import com.cyrillrx.rpg.core.presentation.theme.Purple700
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.spellbook.domain.Spell
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.spell_search_hint

@Composable
fun SpellBookScreen(
    viewModel: SpellBookViewModel,
    navigateToSpell: (Spell) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpellBookScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is SpellListAction.OnSpellClicked -> navigateToSpell(action.spell)
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
fun SpellBookScreen(
    state: SpellListState,
    onAction: (SpellListAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple700)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SpellSearchBar(searchQuery = state.searchQuery, onAction = onAction)

        when (state) {
            is SpellListState.Loading -> Loading()
            is SpellListState.Empty -> Empty(state = state)
            is SpellListState.Error -> Error(state = state)
            is SpellListState.WithData -> SpellList(state = state, onAction = onAction)
        }
    }
}

@Composable
fun AlternativeSpellBookScreen(viewModel: SpellBookViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AlternativeSpellBookScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) },
    )
}

@Composable
fun AlternativeSpellBookScreen(
    state: SpellListState,
    onAction: (SpellListAction) -> Unit,
) {

    Column {
        SpellSearchBar(searchQuery = state.searchQuery, onAction = onAction)

        when (state) {
            is SpellListState.Loading -> Loading()
            is SpellListState.Empty -> Empty(state = state)
            is SpellListState.Error -> Error(state = state)
            is SpellListState.WithData -> AlternativeSpellList(state = state)
        }
    }
}

@Composable
private fun SpellSearchBar(searchQuery: String, onAction: (SpellListAction) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchBar(
        hint = stringResource(Res.string.spell_search_hint),
        query = searchQuery,
        onQueryChanged = { onAction(SpellListAction.OnSearchQueryChanged(it)) },
        onImeSearch = { keyboardController?.hide() },
        modifier = Modifier.widthIn(max = 400.dp)
            .fillMaxWidth()
            .padding(spacingCommon),
    )
}

@Composable
private fun Loading() {
    Text(
        text = "Loading...",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingCommon),
    )
}

@Composable
private fun Error(state: SpellListState.Error) {
    Text(
        text = "Error: ${state.errorMessage}",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingCommon),
    )
}

@Composable
private fun Empty(state: SpellListState.Empty) {
    Text(
        text = "No results found for '${state.searchQuery}'",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingCommon),
    )
}

@Composable
private fun SpellList(
    state: SpellListState.WithData,
    onAction: (SpellListAction) -> Unit,
) {
    val searchResultsListState = rememberLazyListState()
    LaunchedEffect(state.searchResults) {
        searchResultsListState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = searchResultsListState,
    ) {
        items(state.searchResults) { spell ->

            SpellListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onAction(SpellListAction.OnSpellClicked(spell)) },
                spell = spell,
                isSaved = false,
                onSaveClicked = { onAction(SpellListAction.OnSaveSpellClicked(it)) },
            )
        }
    }
}

@Composable
private fun AlternativeSpellList(state: SpellListState.WithData) {
    val searchResultsListState = rememberLazyListState()
    LaunchedEffect(state.searchResults) {
        searchResultsListState.animateScrollToItem(0)
    }

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        state = searchResultsListState,
    ) {
        items(state.searchResults) { spell ->
            Box(modifier = Modifier.fillParentMaxSize()) {
                SpellCard(spell)
            }
        }
    }
}
