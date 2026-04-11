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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBarWithBack
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListState
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRouter
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_magical_item

@Composable
fun MagicalItemCardCarouselScreen(viewModel: MagicalItemListViewModel, router: MagicalItemRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MagicalItemCardCarouselScreen(
        state = state,
        onNavigateUpClicked = router::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onMagicalItemClicked = viewModel::onItemClicked,
        onTypeToggled = viewModel::onTypeToggled,
        onRarityToggled = viewModel::onRarityToggled,
        onResetFilters = viewModel::onResetFilters,
    )
}

@Composable
fun MagicalItemCardCarouselScreen(
    state: MagicalItemListState,
    onNavigateUpClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onMagicalItemClicked: (MagicalItem) -> Unit,
    onTypeToggled: (MagicalItem.Type) -> Unit,
    onRarityToggled: (MagicalItem.Rarity) -> Unit,
    onResetFilters: () -> Unit,
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBarWithBack(
                hint = stringResource(Res.string.hint_search_magical_item),
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
                is MagicalItemListState.Body.Loading -> Loader()
                is MagicalItemListState.Body.Empty -> EmptySearch(state.filter.query)
                is MagicalItemListState.Body.Error -> ErrorLayout(body.errorMessage)
                is MagicalItemListState.Body.WithData -> MagicalItemCardCarousel(
                    body.searchResults,
                    onMagicalItemClicked,
                )
            }
        }
    }

    if (showFilterSheet) {
        MagicalItemFilterBottomSheet(
            filter = state.filter,
            onTypeToggled = onTypeToggled,
            onRarityToggled = onRarityToggled,
            onResetFilters = onResetFilters,
            onDismiss = { showFilterSheet = false },
        )
    }
}

@Composable
private fun MagicalItemCardCarousel(
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
    body = MagicalItemListState.Body.WithData(SampleMagicalItemRepository.getAll()),
)

@Preview
@Composable
private fun PreviewMagicalItemCardCarouselScreenLight() {
    AppThemePreview(darkTheme = false) {
        MagicalItemCardCarouselScreen(stateWithSampleData, {}, {}, {}, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewMagicalItemCardCarouselScreenDark() {
    AppThemePreview(darkTheme = true) {
        MagicalItemCardCarouselScreen(stateWithSampleData, {}, {}, {}, {}, {}, {})
    }
}
