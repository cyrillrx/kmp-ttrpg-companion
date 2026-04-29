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
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.presentation.MonsterAddToListProvider
import com.cyrillrx.rpg.creature.presentation.MonsterListState
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRouter
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterListViewModel
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_creature

@Composable
fun MonsterListScreen(
    viewModel: MonsterListViewModel,
    router: MonsterRouter,
    addToListProvider: AddToListProvider<Monster>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MonsterListScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onMonsterClicked = viewModel::onMonsterClicked,
        onTypeToggled = viewModel::onTypeToggled,
        onChallengeRatingToggled = viewModel::onChallengeRatingToggled,
        onResetFilters = viewModel::onResetFilters,
        addToListProvider = addToListProvider,
    )
}

@Composable
fun MonsterListScreen(
    state: MonsterListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onMonsterClicked: (Monster) -> Unit,
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
                is MonsterListState.Body.Loading -> Loader()
                is MonsterListState.Body.Empty -> EmptySearch(state.filter.query)
                is MonsterListState.Body.Error -> ErrorLayout(body.errorMessage)
                is MonsterListState.Body.WithData -> MonsterCompactList(
                    creatures = body.searchResults,
                    onMonsterClicked = onMonsterClicked,
                    showAddToList = { creature -> creatureToAdd = creature },
                )
            }
        }
    }

    if (showFilterSheet) {
        MonsterFilterBottomSheet(
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
private fun MonsterCompactList(
    creatures: List<Monster>,
    onMonsterClicked: (Monster) -> Unit,
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
                MonsterListItem(
                    creature = creature,
                    onClick = { onMonsterClicked(creature) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMonsterListScreenLight() {
    AppThemePreview(darkTheme = false) {
        MonsterListScreenPreview()
    }
}

@Preview
@Composable
private fun PreviewMonsterListScreenDark() {
    AppThemePreview(darkTheme = true) {
        MonsterListScreenPreview()
    }
}

@Composable
private fun MonsterListScreenPreview() {
    val stateWithSampleData = MonsterListState(
        body = MonsterListState.Body.WithData(SampleMonsterRepository.getAll()),
    )
    val addToListProvider = MonsterAddToListProvider(SampleMonsterRepository(), SampleUserListRepository())
    MonsterListScreen(stateWithSampleData, {}, {}, {}, {}, {}, {}, addToListProvider)
}
