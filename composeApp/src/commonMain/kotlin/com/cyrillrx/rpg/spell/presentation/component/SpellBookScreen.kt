package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.componenent.EmptySearch
import com.cyrillrx.rpg.core.presentation.componenent.ErrorLayout
import com.cyrillrx.rpg.core.presentation.componenent.Loader
import com.cyrillrx.rpg.core.presentation.componenent.SearchBar
import com.cyrillrx.rpg.core.presentation.theme.Purple700
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellBookViewModel
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_spell

@Composable
fun SpellBookScreen(viewModel: SpellBookViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpellBookScreen(
        state = state,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSpellClicked = viewModel::onSpellClicked,
        onSpellSaved = viewModel::onSpellSaved,
    )
}

@Composable
fun SpellBookScreen(
    state: SpellListState,
    onSearchQueryChanged: (String) -> Unit,
    onSpellClicked: (Spell) -> Unit,
    onSpellSaved: (Spell) -> Unit,
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
            onQueryChanged = onSearchQueryChanged,
        )

        when (val body = state.body) {
            is SpellListState.Body.Loading -> Loader()
            is SpellListState.Body.Empty -> EmptySearch(state.searchQuery)
            is SpellListState.Body.Error -> ErrorLayout(body.errorMessage)
            is SpellListState.Body.WithData -> SpellList(body.searchResults, onSpellClicked, onSpellSaved)
        }
    }
}

@Composable
fun AlternativeSpellBookScreen(viewModel: SpellBookViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AlternativeSpellBookScreen(
        state = state,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
    )
}

@Composable
fun AlternativeSpellBookScreen(
    state: SpellListState,
    onSearchQueryChanged: (String) -> Unit,
) {

    Column {
        SearchBar(
            hint = stringResource(Res.string.hint_search_spell),
            query = state.searchQuery,
            onQueryChanged = onSearchQueryChanged,
        )

        when (val body = state.body) {
            is SpellListState.Body.Loading -> Loader()
            is SpellListState.Body.Empty -> EmptySearch(state.searchQuery)
            is SpellListState.Body.Error -> ErrorLayout(body.errorMessage)
            is SpellListState.Body.WithData -> AlternativeSpellList(state = body)
        }
    }
}

@Composable
private fun SpellList(
    spells: List<Spell>,
    onSpellClicked: (Spell) -> Unit,
    onSpellSaved: (Spell) -> Unit,
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
                    .clickable { onSpellClicked(spell) },
                spell = spell,
                isSaved = false,
                onSaveClicked = onSpellSaved,
            )
        }
    }
}

@Composable
private fun AlternativeSpellList(state: SpellListState.Body.WithData) {
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
