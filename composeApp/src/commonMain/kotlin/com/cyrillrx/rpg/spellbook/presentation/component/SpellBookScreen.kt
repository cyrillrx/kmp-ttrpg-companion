package com.cyrillrx.rpg.spellbook.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.componenent.SearchBar
import com.cyrillrx.rpg.core.presentation.theme.Purple700
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.spellbook.domain.Spell
import com.cyrillrx.rpg.spellbook.presentation.SpellListAction
import com.cyrillrx.rpg.spellbook.presentation.SpellListState
import com.cyrillrx.rpg.spellbook.presentation.viewmodel.SpellBookViewModel
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_spell

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
        SearchBar(
            hint = stringResource(Res.string.hint_search_spell),
            query = state.searchQuery,
            onQueryChanged = { onAction(SpellListAction.OnSearchQueryChanged(it)) },
        )

        when (state) {
            is SpellListState.Loading -> Loading()
            is SpellListState.Empty -> Empty(state)
            is SpellListState.Error -> Error(state)
            is SpellListState.WithData -> SpellList(state.searchResults, onAction)
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
        SearchBar(
            hint = stringResource(Res.string.hint_search_spell),
            query = state.searchQuery,
            onQueryChanged = { onAction(SpellListAction.OnSearchQueryChanged(it)) },
        )

        when (state) {
            is SpellListState.Loading -> Loading()
            is SpellListState.Empty -> Empty(state = state)
            is SpellListState.Error -> Error(state = state)
            is SpellListState.WithData -> AlternativeSpellList(state = state)
        }
    }
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
    spells: List<Spell>,
    onAction: (SpellListAction) -> Unit,
) {
    val searchResultsListState = rememberLazyListState()
    LaunchedEffect(spells) {
        searchResultsListState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = searchResultsListState,
    ) {
        items(spells) { spell ->
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
