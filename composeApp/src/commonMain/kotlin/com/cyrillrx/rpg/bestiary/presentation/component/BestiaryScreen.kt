package com.cyrillrx.rpg.bestiary.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.bestiary.domain.Creature
import com.cyrillrx.rpg.bestiary.presentation.BestiaryAction
import com.cyrillrx.rpg.bestiary.presentation.BestiaryState
import com.cyrillrx.rpg.bestiary.presentation.viewmodel.BestiaryViewModel
import com.cyrillrx.rpg.core.presentation.componenent.EmptySearch
import com.cyrillrx.rpg.core.presentation.componenent.Loader
import com.cyrillrx.rpg.core.presentation.componenent.ErrorLayout
import com.cyrillrx.rpg.core.presentation.componenent.SearchBar
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_creature

@Composable
fun BestiaryScreen(
    viewModel: BestiaryViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BestiaryScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) },
    )
}

@Composable
fun BestiaryScreen(
    state: BestiaryState,
    onAction: (BestiaryAction) -> Unit,
) {
    Column {
        SearchBar(
            hint = stringResource(Res.string.hint_search_creature),
            query = state.searchQuery,
            onQueryChanged = { onAction(BestiaryAction.OnSearchQueryChanged(it)) },
        )

        when (state) {
            is BestiaryState.Loading -> Loader()
            is BestiaryState.Empty -> EmptySearch(state.searchQuery)
            is BestiaryState.Error -> ErrorLayout(state.errorMessage)
            is BestiaryState.WithData -> CreatureList(state.searchResults, onAction)
        }
    }
}

@Composable
private fun CreatureList(
    creatures: List<Creature>,
    onAction: (BestiaryAction) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(creatures) { creature ->
            CreatureItem(creature)
            HorizontalDivider()
        }
    }
}
