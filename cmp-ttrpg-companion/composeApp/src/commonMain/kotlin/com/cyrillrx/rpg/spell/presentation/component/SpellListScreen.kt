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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBarWithBack
import com.cyrillrx.rpg.core.presentation.component.SwipeToAddBox
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.SpellAddToListProvider
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModel
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_spell

@Composable
fun SpellListScreen(
    viewModel: SpellListViewModel,
    router: SpellRouter,
    addToListProvider: AddToListProvider<Spell>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpellListScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSpellClicked = { spell -> router.openDetail(spell.id) },
        onLevelToggled = viewModel::onLevelToggled,
        onSchoolToggled = viewModel::onSchoolToggled,
        onClassToggled = viewModel::onClassToggled,
        onResetFilters = viewModel::onResetFilters,
        addToListProvider = addToListProvider,
    )
}

@Composable
fun SpellListScreen(
    state: SpellListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSpellClicked: (Spell) -> Unit,
    onLevelToggled: (Int) -> Unit,
    onSchoolToggled: (Spell.School) -> Unit,
    onClassToggled: (PlayerCharacter.Class) -> Unit,
    onResetFilters: () -> Unit,
    addToListProvider: AddToListProvider<Spell>,
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    var spellToAdd by remember { mutableStateOf<Spell?>(null) }

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
                is SpellListState.Body.WithData -> SpellList(
                    spells = body.searchResults,
                    onSpellClicked = onSpellClicked,
                    showAddToList = { spell -> spellToAdd = spell },
                )
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

    spellToAdd?.let { spell ->
        key(spell) {
            addToListProvider.BottomSheet(
                entityId = spell.id,
                onDismiss = { spellToAdd = null },
            )
        }
    }
}

@Composable
private fun SpellList(
    spells: List<Spell>,
    onSpellClicked: (Spell) -> Unit,
    showAddToList: (Spell) -> Unit,
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
        items(spells, key = { it.id }) { spell ->
            SwipeToAddBox(onSwiped = { showAddToList(spell) }) {
                SpellListItem(
                    spell = spell,
                    onClick = { onSpellClicked(spell) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSpellListPeekScreenLight() {
    AppThemePreview(darkTheme = false) { SpellListPeekScreenPreview() }
}

@Preview
@Composable
fun PreviewSpellListPeekScreenDark() {
    AppThemePreview(darkTheme = true) { SpellListPeekScreenPreview() }
}

@Composable
private fun SpellListPeekScreenPreview() {
    val userListRepository = SampleUserListRepository()
    val spellRepository = SampleSpellRepository()
    val stateWithSampleData = SpellListState(
        body = SpellListState.Body.WithData(SampleSpellRepository.getAll()),
    )
    val bottomSheetProvider = SpellAddToListProvider(spellRepository, userListRepository)

    SpellListScreen(stateWithSampleData, {}, {}, {}, {}, {}, {}, {}, bottomSheetProvider)
}
