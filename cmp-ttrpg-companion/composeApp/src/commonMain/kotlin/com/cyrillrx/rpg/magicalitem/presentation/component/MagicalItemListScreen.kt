package com.cyrillrx.rpg.magicalitem.presentation.component

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
                query = state.filter.query,
                onQueryChanged = onSearchQueryChanged,
                onNavigateUpClicked = onNavigateUpClicked,
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
                is MagicalItemListState.Body.Loading -> Loader()
                is MagicalItemListState.Body.Empty -> EmptySearch(state.filter.query)
                is MagicalItemListState.Body.Error -> ErrorLayout(body.errorMessage)
                is MagicalItemListState.Body.WithData -> MagicalItemList(body.searchResults, onMagicalItemClicked)
            }
        }
    }
}

@Composable
private fun MagicalItemList(
    magicalItems: List<MagicalItem>,
    onMagicalItemClicked: (MagicalItem) -> Unit,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(magicalItems) {
        listState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(magicalItems) { item ->
            MagicalItemListItem(
                magicalItem = item,
                onClick = { onMagicalItemClicked(item) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private val stateWithSampleData = MagicalItemListState(
    body = MagicalItemListState.Body.WithData(SampleMagicalItemsRepository().getAll()),
)

@Preview
@Composable
private fun PreviewMagicalItemListScreenLight() {
    AppThemePreview(darkTheme = false) {
        MagicalItemListScreen(stateWithSampleData, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewMagicalItemListScreenDark() {
    AppThemePreview(darkTheme = true) {
        MagicalItemListScreen(stateWithSampleData, {}, {}, {})
    }
}
