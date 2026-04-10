package com.cyrillrx.rpg.creature.presentation.component

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.presentation.CreatureListState
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRouter
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_creature

@Composable
fun CreatureCompactListScreen(viewModel: CreatureListViewModel, router: CreatureRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreatureCompactListScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCreatureClicked = viewModel::onCreatureClicked,
        onTypeToggled = viewModel::onTypeToggled,
        onChallengeRatingToggled = viewModel::onChallengeRatingToggled,
        onResetFilters = viewModel::onResetFilters,
    )
}

@Composable
fun CreatureCompactListScreen(
    state: CreatureListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCreatureClicked: (Creature) -> Unit,
    onTypeToggled: (Creature.Type) -> Unit,
    onChallengeRatingToggled: (Float) -> Unit,
    onResetFilters: () -> Unit,
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBarWithBack(
                hint = stringResource(Res.string.hint_search_creature),
                query = state.filter.query,
                onQueryChanged = onSearchQueryChanged,
                onNavigateUpClicked = onNavigateUpClicked,
                onFilterClicked = { showFilterSheet = true },
                hasActiveFilters = state.filter.hasActiveFilters,
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
                is CreatureListState.Body.Loading -> Loader()
                is CreatureListState.Body.Empty -> EmptySearch(state.filter.query)
                is CreatureListState.Body.Error -> ErrorLayout(body.errorMessage)
                is CreatureListState.Body.WithData -> CreatureCompactList(body.searchResults, onCreatureClicked)
            }
        }
    }

    if (showFilterSheet) {
        CreatureFilterBottomSheet(
            filter = state.filter,
            onTypeToggled = onTypeToggled,
            onChallengeRatingToggled = onChallengeRatingToggled,
            onResetFilters = onResetFilters,
            onDismiss = { showFilterSheet = false },
        )
    }
}

@Composable
private fun CreatureCompactList(
    creatures: List<Creature>,
    onCreatureClicked: (Creature) -> Unit,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(creatures) {
        listState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(creatures) { creature ->
            CreatureCompactListItem(
                creature = creature,
                onClick = { onCreatureClicked(creature) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private val stateWithSampleData = CreatureListState(
    body = CreatureListState.Body.WithData(SampleCreatureRepository.getAll()),
)

@Preview
@Composable
private fun PreviewCreatureCompactListScreenLight() {
    AppThemePreview(darkTheme = false) {
        CreatureCompactListScreen(stateWithSampleData, {}, {}, {}, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewCreatureCompactListScreenDark() {
    AppThemePreview(darkTheme = true) {
        CreatureCompactListScreen(stateWithSampleData, {}, {}, {}, {}, {}, {})
    }
}
