package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.SpellListDetailState
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListDetailViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.no_result_found

@Composable
fun SpellListDetailScreen(
    viewModel: SpellListDetailViewModel,
    onNavigateUpClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SpellListDetailScreen(
        state = state,
        onNavigateUpClicked = onNavigateUpClicked,
        onRemoveSpell = viewModel::removeSpell,
    )
}

@Composable
fun SpellListDetailScreen(
    state: SpellListDetailState,
    onNavigateUpClicked: () -> Unit,
    onRemoveSpell: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = state.listName,
                navigateUp = onNavigateUpClicked,
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
                is SpellListDetailState.Body.Loading -> Loader()
                is SpellListDetailState.Body.Empty -> {
                    Text(
                        text = stringResource(Res.string.no_result_found),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(spacingMedium),
                    )
                }
                is SpellListDetailState.Body.WithData -> SpellDetailList(
                    spells = body.spells,
                    onRemoveSpell = onRemoveSpell,
                )
            }
        }
    }
}

@Composable
private fun SpellDetailList(
    spells: List<Spell>,
    onRemoveSpell: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(spells, key = { it.id }) { spell ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                SpellListItem(
                    spell = spell,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { onRemoveSpell(spell.id) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSpellListDetailScreenLight() {
    AppThemePreview(darkTheme = false) {
        SpellListDetailScreen(
            state = SpellListDetailState(
                listName = "Sorts de combat",
                body = SpellListDetailState.Body.WithData(SampleSpellRepository.getAll()),
            ),
            onNavigateUpClicked = {},
            onRemoveSpell = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSpellListDetailScreenDark() {
    AppThemePreview(darkTheme = true) {
        SpellListDetailScreen(
            state = SpellListDetailState(
                listName = "Sorts de combat",
                body = SpellListDetailState.Body.Empty,
            ),
            onNavigateUpClicked = {},
            onRemoveSpell = {},
        )
    }
}
