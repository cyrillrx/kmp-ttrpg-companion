package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBarWithBack
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellBookViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_spell

@Composable
fun SpellListScreen(viewModel: SpellBookViewModel, router: SpellRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpellListScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSpellClicked = router::openSpellDetail,
    )
}

@Composable
fun SpellListScreen(
    state: SpellListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSpellClicked: (Spell) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchBarWithBack(
                hint = stringResource(Res.string.hint_search_spell),
                query = state.filter.query,
                onQueryChanged = onSearchQueryChanged,
                onNavigateUpClicked = onNavigateUpClicked,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val body = state.body) {
                is SpellListState.Body.Loading -> Loader()
                is SpellListState.Body.Empty -> EmptySearch(state.filter.query)
                is SpellListState.Body.Error -> ErrorLayout(body.errorMessage)
                is SpellListState.Body.WithData -> SpellList(body.searchResults, onSpellClicked)
            }
        }
    }
}

@Composable
private fun SpellList(
    spells: List<Spell>,
    onSpellClicked: (Spell) -> Unit,
) {
    val searchResultsListState = rememberLazyListState()
    LaunchedEffect(spells) {
        searchResultsListState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = searchResultsListState,
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(spells) { spell ->
            SpellListItem(
                spell = spell,
                onClick = { onSpellClicked(spell) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private val stateWithSampleData = SpellListState(
    body = SpellListState.Body.WithData(SampleSpellRepository().getAll()),
)

@Preview
@Composable
fun PreviewSpellBookPeekScreenLight() {
    AppThemePreview(darkTheme = false) {
        SpellListScreen(stateWithSampleData, {}, {}, {})
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenDark() {
    AppThemePreview(darkTheme = true) {
        SpellListScreen(stateWithSampleData, {}, {}, {})
    }
}
