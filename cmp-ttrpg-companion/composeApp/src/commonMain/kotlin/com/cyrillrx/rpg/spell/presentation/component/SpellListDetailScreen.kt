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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.ConfirmDeleteDialog
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
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
import rpg_companion.composeapp.generated.resources.dialog_remove_from_list_message
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
        onRemoveSpellClicked = viewModel::removeSpell,
    )
}

@Composable
fun SpellListDetailScreen(
    state: SpellListDetailState,
    onNavigateUpClicked: () -> Unit,
    onRemoveSpellClicked: (String) -> Unit,
) {
    var spellToRemove by remember { mutableStateOf<Spell?>(null) }

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
                is SpellListDetailState.Body.EmptyList -> ErrorLayout(Res.string.no_result_found)
                is SpellListDetailState.Body.Error -> ErrorLayout(body.errorMessage)
                is SpellListDetailState.Body.WithData -> SpellDetailList(
                    spells = body.spells,
                    onRemoveSpell = { spellToRemove = it },
                )
            }
        }
    }

    spellToRemove?.let { spell ->
        ConfirmDeleteDialog(
            message = stringResource(Res.string.dialog_remove_from_list_message, spell.title),
            onConfirm = {
                onRemoveSpellClicked(spell.id)
                spellToRemove = null
            },
            onDismiss = { spellToRemove = null },
        )
    }
}

@Composable
private fun SpellDetailList(
    spells: List<Spell>,
    onRemoveSpell: (Spell) -> Unit,
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
                IconButton(onClick = { onRemoveSpell(spell) }) {
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
    SpellListDetailScreenPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewSpellListDetailScreenDark() {
    SpellListDetailScreenPreview(darkTheme = true)
}

@Composable
private fun SpellListDetailScreenPreview(darkTheme: Boolean) {
    val spells = SampleSpellRepository.getAll()
    AppThemePreview(darkTheme = darkTheme) {
        SpellListDetailScreen(
            state = SpellListDetailState(
                listName = "Gandalf's spells",
                body = SpellListDetailState.Body.WithData(spells),
            ),
            onNavigateUpClicked = {},
            onRemoveSpellClicked = {},
        )
    }
}
