package com.cyrillrx.rpg.magicalitem.presentation.component

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
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemsRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListState
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_magical_item

@Composable
fun MagicalItemListScreen(viewModel: MagicalItemListViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MagicalItemListScreen(
        state = state,
        onNavigateUpClicked = viewModel::onNavigateUpClicked,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onMagicalItemClicked = viewModel::onItemClicked,
    )
}

@Composable
fun MagicalItemListScreen(
    state: MagicalItemListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onMagicalItemClicked: (MagicalItem) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchBarWithBack(
                hint = stringResource(Res.string.hint_search_magical_item),
                query = state.searchQuery,
                onQueryChanged = onSearchQueryChanged,
                onNavigateUpClicked = onNavigateUpClicked,
            )
        },
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            when (val body = state.body) {
                is MagicalItemListState.Body.Loading -> Loader()
                is MagicalItemListState.Body.Empty -> EmptySearch(state.searchQuery)
                is MagicalItemListState.Body.Error -> ErrorLayout(body.errorMessage)
                is MagicalItemListState.Body.WithData -> MagicalItemsList(body.searchResults, onMagicalItemClicked)
            }
        }
    }
}

@Composable
private fun MagicalItemsList(
    magicalItems: List<MagicalItem>,
    onMagicalItemClicked: (MagicalItem) -> Unit,
) {
    val searchResultsListState = rememberLazyListState()
    LaunchedEffect(magicalItems) {
        searchResultsListState.animateScrollToItem(0)
    }

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        state = searchResultsListState,
    ) {
        items(magicalItems) { item ->
            Box(modifier = Modifier.fillParentMaxSize()) {
                MagicalItemCard(
                    magicalItem = item,
                    modifier = Modifier.clickable { onMagicalItemClicked(item) },
                )
            }
        }
    }
}

private val stateWithSampleData = MagicalItemListState(
    searchQuery = "",
    body = MagicalItemListState.Body.WithData(SampleMagicalItemsRepository().getAll()),
)

@Preview
@Composable
private fun PreviewSpellBookScreenLight() {
    AppThemePreview(darkTheme = false) {
        MagicalItemListScreen(stateWithSampleData, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewSpellBookScreenDark() {
    AppThemePreview(darkTheme = true) {
        MagicalItemListScreen(stateWithSampleData, {}, {}, {})
    }
}
