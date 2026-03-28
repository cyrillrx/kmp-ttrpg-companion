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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBarWithBack
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_spell

@Composable
fun SpellCardCarouselScreen(viewModel: SpellListViewModel, router: SpellRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpellCardCarouselScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSpellClicked = router::openSpellDetail,
        onLevelToggled = viewModel::onLevelToggled,
        onSchoolToggled = viewModel::onSchoolToggled,
        onClassToggled = viewModel::onClassToggled,
        onResetFilters = viewModel::onResetFilters,
    )
}

@Composable
fun SpellCardCarouselScreen(
    state: SpellListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSpellClicked: (Spell) -> Unit,
    onLevelToggled: (Int) -> Unit,
    onSchoolToggled: (Spell.School) -> Unit,
    onClassToggled: (PlayerCharacter.Class) -> Unit,
    onResetFilters: () -> Unit,
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBarWithBack(
                hint = stringResource(Res.string.hint_search_spell),
                query = state.filter.query,
                onQueryChanged = onSearchQueryChanged,
                onNavigateUpClicked = onNavigateUpClicked,
                onFilterClicked = { showFilterSheet = true },
                hasActiveFilters = state.filter.hasActiveFilters,
            )
        },
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            when (val body = state.body) {
                is SpellListState.Body.Loading -> Loader()
                is SpellListState.Body.Empty -> EmptySearch(state.filter.query)
                is SpellListState.Body.Error -> ErrorLayout(body.errorMessage)
                is SpellListState.Body.WithData -> SpellCardCarousel(body.searchResults, onSpellClicked)
            }
        }
    }

    if (showFilterSheet) {
        SpellFilterBottomSheet(
            filter = state.filter,
            onLevelToggled = onLevelToggled,
            onSchoolToggled = onSchoolToggled,
            onClassToggled = onClassToggled,
            onResetFilters = onResetFilters,
            onDismiss = { showFilterSheet = false },
        )
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
    body = SpellListState.Body.WithData(SampleSpellRepository.getAll()),
)

@Preview
@Composable
fun PreviewSpellCardCarouselScreenLight() {
    AppThemePreview(darkTheme = false) {
        SpellCardCarouselScreen(stateWithSampleData, {}, {}, {}, {}, {}, {}, {})
    }
}

@Preview
@Composable
fun PreviewSpellCardCarouselScreenDark() {
    AppThemePreview(darkTheme = true) {
        SpellCardCarouselScreen(stateWithSampleData, {}, {}, {}, {}, {}, {}, {})
    }
}
