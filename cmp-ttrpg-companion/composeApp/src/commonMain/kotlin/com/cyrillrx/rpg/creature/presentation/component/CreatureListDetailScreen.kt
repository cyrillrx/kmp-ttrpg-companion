package com.cyrillrx.rpg.creature.presentation.component

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
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.component.dialog.ConfirmDeleteDialog
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.presentation.CreatureListDetailState
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureListDetailViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.dialog_remove_from_list_message
import rpg_companion.composeapp.generated.resources.message_list_is_empty

@Composable
fun CreatureListDetailScreen(
    viewModel: CreatureListDetailViewModel,
    onNavigateUpClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreatureListDetailScreen(
        state = state,
        onNavigateUpClicked = onNavigateUpClicked,
        onRemoveCreatureClicked = viewModel::removeCreature,
    )
}

@Composable
fun CreatureListDetailScreen(
    state: CreatureListDetailState,
    onNavigateUpClicked: () -> Unit,
    onRemoveCreatureClicked: (String) -> Unit,
) {
    var itemToRemove by remember { mutableStateOf<Creature?>(null) }

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
                is CreatureListDetailState.Body.Loading -> Loader()
                is CreatureListDetailState.Body.EmptyList -> ErrorLayout(Res.string.message_list_is_empty)
                is CreatureListDetailState.Body.Error -> ErrorLayout(body.errorMessage)
                is CreatureListDetailState.Body.WithData -> CreatureDetailList(
                    creatures = body.creatures,
                    onRemoveCreature = { itemToRemove = it },
                )
            }
        }
    }

    itemToRemove?.let { creature ->
        ConfirmDeleteDialog(
            message = stringResource(Res.string.dialog_remove_from_list_message, creature.name),
            onConfirm = {
                onRemoveCreatureClicked(creature.id)
                itemToRemove = null
            },
            onDismiss = { itemToRemove = null },
        )
    }
}

@Composable
private fun CreatureDetailList(
    creatures: List<Creature>,
    onRemoveCreature: (Creature) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        items(creatures, key = { it.id }) { creature ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                CreatureCompactListItem(
                    creature = creature,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { onRemoveCreature(creature) }) {
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
private fun PreviewCreatureListDetailScreenLight() {
    CreatureListDetailScreenPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewCreatureListDetailScreenDark() {
    CreatureListDetailScreenPreview(darkTheme = true)
}

@Composable
private fun CreatureListDetailScreenPreview(darkTheme: Boolean) {
    val creatures = SampleCreatureRepository.getAll()
    AppThemePreview(darkTheme = darkTheme) {
        CreatureListDetailScreen(
            state = CreatureListDetailState(
                listName = "Bestiary",
                body = CreatureListDetailState.Body.WithData(creatures),
            ),
            onNavigateUpClicked = {},
            onRemoveCreatureClicked = {},
        )
    }
}
