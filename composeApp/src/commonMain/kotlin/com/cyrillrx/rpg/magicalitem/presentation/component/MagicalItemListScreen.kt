package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBar
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListState
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemListViewModel
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_magical_item

@Composable
fun MagicalItemListScreen(viewModel: MagicalItemListViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MagicalItemListScreen(
        state = state,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onMagicalItemClicked = viewModel::onItemClicked,
    )
}

@Composable
fun MagicalItemListScreen(
    state: MagicalItemListState,
    onSearchQueryChanged: (String) -> Unit,
    onMagicalItemClicked: (MagicalItem) -> Unit,
) {
    Column {
        SearchBar(
            hint = stringResource(Res.string.hint_search_magical_item),
            query = state.searchQuery,
            onQueryChanged = onSearchQueryChanged,
        )

        when (val body = state.body) {
            is MagicalItemListState.Body.Loading -> Loader()
            is MagicalItemListState.Body.Empty -> EmptySearch(state.searchQuery)
            is MagicalItemListState.Body.Error -> ErrorLayout(body.errorMessage)
            is MagicalItemListState.Body.WithData -> MagicalItemsList(body.searchResults, onMagicalItemClicked)
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
