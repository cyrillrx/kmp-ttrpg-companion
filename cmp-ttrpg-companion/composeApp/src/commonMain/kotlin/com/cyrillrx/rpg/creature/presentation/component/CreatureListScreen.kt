package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBarWithBack
import com.cyrillrx.rpg.core.presentation.component.SwipeToAdd
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.presentation.CreatureAddToListProvider
import com.cyrillrx.rpg.creature.presentation.CreatureListState
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_creature

@Composable
fun CreatureListScreen(
    viewModel: CreatureListViewModel,
    addToListProvider: AddToListProvider<Creature>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreatureListScreen(
        state = state,
        onNavigateUpClicked = viewModel::onNavigateUpClicked,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCreatureClicked = viewModel::onCreatureClicked,
        onTypeToggled = viewModel::onTypeToggled,
        onChallengeRatingToggled = viewModel::onChallengeRatingToggled,
        onResetFilters = viewModel::onResetFilters,
        addToListProvider = addToListProvider,
    )
}

@Composable
fun CreatureListScreen(
    state: CreatureListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCreatureClicked: (Creature) -> Unit,
    onTypeToggled: (Creature.Type) -> Unit,
    onChallengeRatingToggled: (Float) -> Unit,
    onResetFilters: () -> Unit,
    addToListProvider: AddToListProvider<Creature>,
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    var creatureToAdd by remember { mutableStateOf<Creature?>(null) }

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
        Column(Modifier.padding(paddingValues)) {
            when (val body = state.body) {
                is CreatureListState.Body.Loading -> Loader()
                is CreatureListState.Body.Empty -> EmptySearch(state.filter.query)
                is CreatureListState.Body.Error -> ErrorLayout(body.errorMessage)
                is CreatureListState.Body.WithData -> CreatureList(
                    creatures = body.searchResults,
                    onCreatureClicked = onCreatureClicked,
                    showAddToList = { creature -> creatureToAdd = creature },
                )
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

    creatureToAdd?.let { creature ->
        addToListProvider.BottomSheet(
            entityId = creature.id,
            onDismiss = { creatureToAdd = null },
        )
    }
}

@Composable
private fun CreatureList(
    creatures: List<Creature>,
    onCreatureClicked: (Creature) -> Unit,
    showAddToList: (Creature) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(creatures, key = { it.id }) { creature ->
            SwipeToAdd(
                onSwiped = { showAddToList(creature) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                CreatureItem(
                    creature = creature,
                    modifier = Modifier.fillMaxWidth().clickable { onCreatureClicked(creature) },
                )
            }
            HorizontalDivider()
        }
    }
}

private val stateWithSampleData = CreatureListState(
    body = CreatureListState.Body.WithData(SampleCreatureRepository.getAll()),
)

@Preview
@Composable
private fun PreviewCreatureListScreenLight() {
    AppThemePreview(darkTheme = false) { CreatureListScreenPreview() }
}

@Preview
@Composable
private fun PreviewCreatureListScreenDark() {
    AppThemePreview(darkTheme = true) { CreatureListScreenPreview() }
}

@Composable
private fun CreatureListScreenPreview() {
    val addToListProvider = CreatureAddToListProvider(SampleCreatureRepository(), SampleUserListRepository())
    CreatureListScreen(stateWithSampleData, {}, {}, {}, {}, {}, {}, addToListProvider)
}
