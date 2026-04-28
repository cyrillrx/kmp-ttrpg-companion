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
import com.cyrillrx.rpg.core.presentation.component.SwipeToAdd
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.presentation.CreatureAddToListProvider
import com.cyrillrx.rpg.creature.presentation.CreatureListState
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRouter
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_creature

@Composable
fun CreatureCompactListScreen(
    viewModel: CreatureListViewModel,
    router: CreatureRouter,
    addToListProvider: AddToListProvider<Monster>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreatureCompactListScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCreatureClicked = viewModel::onCreatureClicked,
        onTypeToggled = viewModel::onTypeToggled,
        onChallengeRatingToggled = viewModel::onChallengeRatingToggled,
        onResetFilters = viewModel::onResetFilters,
        addToListProvider = addToListProvider,
    )
}

@Composable
fun CreatureCompactListScreen(
    state: CreatureListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCreatureClicked: (Monster) -> Unit,
    onTypeToggled: (Monster.Type) -> Unit,
    onChallengeRatingToggled: (Float) -> Unit,
    onResetFilters: () -> Unit,
    addToListProvider: AddToListProvider<Monster>,
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    var creatureToAdd by remember { mutableStateOf<Monster?>(null) }

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
                is CreatureListState.Body.WithData -> CreatureCompactList(
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
private fun CreatureCompactList(
    creatures: List<Monster>,
    onCreatureClicked: (Monster) -> Unit,
    showAddToList: (Monster) -> Unit,
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
        items(creatures, key = { it.id }) { creature ->
            SwipeToAdd(
                onSwiped = { showAddToList(creature) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                CreatureCompactListItem(
                    creature = creature,
                    onClick = { onCreatureClicked(creature) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreatureCompactListScreenLight() {
    AppThemePreview(darkTheme = false) {
        CreatureCompactListScreenPreview()
    }
}

@Preview
@Composable
private fun PreviewCreatureCompactListScreenDark() {
    AppThemePreview(darkTheme = true) {
        CreatureCompactListScreenPreview()
    }
}

@Composable
private fun CreatureCompactListScreenPreview() {
    val stateWithSampleData = CreatureListState(
        body = CreatureListState.Body.WithData(SampleCreatureRepository.getAll()),
    )
    val addToListProvider = CreatureAddToListProvider(SampleCreatureRepository(), SampleUserListRepository())
    CreatureCompactListScreen(stateWithSampleData, {}, {}, {}, {}, {}, {}, addToListProvider)
}
