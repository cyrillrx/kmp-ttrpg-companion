package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBarWithBack
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
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
fun SpellCardCarouselScreen(viewModel: SpellBookViewModel, router: SpellRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpellCardCarouselScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSpellClicked = router::openSpellDetail,
    )
}

@Composable
fun SpellCardCarouselScreen(
    state: SpellListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSpellClicked: (Spell) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchBarWithBack(
                hint = stringResource(Res.string.hint_search_spell),
                query = state.searchQuery,
                onQueryChanged = onSearchQueryChanged,
                onNavigateUpClicked = onNavigateUpClicked,
            )
        },
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            when (val body = state.body) {
                is SpellListState.Body.Loading -> Loader()
                is SpellListState.Body.Empty -> EmptySearch(state.searchQuery)
                is SpellListState.Body.Error -> ErrorLayout(body.errorMessage)
                is SpellListState.Body.WithData -> SpellCardCarousel(body.searchResults, onSpellClicked)
            }
        }
    }
}

@Composable
private fun SpellCardCarousel(
    spells: List<Spell>,
    onSpellClicked: (Spell) -> Unit,
) {
    val searchResultsListState = rememberLazyListState()
    LaunchedEffect(spells) {
        searchResultsListState.animateScrollToItem(0)
    }

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        state = searchResultsListState,
    ) {
        items(spells) { spell ->
            Box(
                modifier = Modifier
                    .fillParentMaxSize()
                    .clickable { onSpellClicked(spell) },
            ) {
                SpellCard(spell)
            }
        }
    }
}

private val stateWithSampleData = SpellListState(
    searchQuery = "",
    body = SpellListState.Body.WithData(SampleSpellRepository().getAll()),
)

@Preview
@Composable
fun PreviewSpellCardCarouselScreenLight() {
    AppThemePreview(darkTheme = false) {
        SpellCardCarouselScreen(stateWithSampleData, {}, {}, {})
    }
}

@Preview
@Composable
fun PreviewSpellCardCarouselScreenDark() {
    AppThemePreview(darkTheme = true) {
        SpellCardCarouselScreen(stateWithSampleData, {}, {}, {})
    }
}
