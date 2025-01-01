package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBar
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.presentation.CreatureListState
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListViewModel
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_creature

@Composable
fun CreatureListScreen(viewModel: CreatureListViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreatureListScreen(
        state = state,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCreatureClicked = viewModel::onCreatureClicked,
    )
}

@Composable
fun CreatureListScreen(
    state: CreatureListState,
    onSearchQueryChanged: (String) -> Unit,
    onCreatureClicked: (Creature) -> Unit,
) {
    Column {
        SearchBar(
            hint = stringResource(Res.string.hint_search_creature),
            query = state.searchQuery,
            onQueryChanged = onSearchQueryChanged,
        )

        when (val body = state.body) {
            is CreatureListState.Body.Loading -> Loader()
            is CreatureListState.Body.Empty -> EmptySearch(state.searchQuery)
            is CreatureListState.Body.Error -> ErrorLayout(body.errorMessage)
            is CreatureListState.Body.WithData -> CreatureList(body.searchResults, onCreatureClicked)
        }
    }
}

@Composable
private fun CreatureList(
    creatures: List<Creature>,
    onCreatureClicked: (Creature) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(creatures) { creature ->
            CreatureItem(
                creature = creature,
//                modifier = Modifier.clickable { onCreatureClicked(creature) },
            )
            HorizontalDivider()
        }
    }
}
