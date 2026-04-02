package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRouter
import com.cyrillrx.rpg.creature.presentation.viewmodel.CreatureDetailViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.error_creature_not_found

@Composable
fun CreatureDetailScreen(
    viewModel: CreatureDetailViewModel,
    router: CreatureRouter,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_creature_not_found, s.id))
        is DetailState.Found -> CreatureDetailScreen(
            creature = s.item,
            onAddToListClicked = router::openAddToList,
            onNavigateUpClicked = router::navigateUp,
        )
    }
}

@Composable
fun CreatureDetailScreen(
    creature: Creature,
    onAddToListClicked: (creatureId: String) -> Unit,
    onNavigateUpClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = creature.name,
                navigateUp = onNavigateUpClicked,
            )
        },
        bottomBar = {
            Button(
                onClick = { onAddToListClicked(creature.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium),
            ) {
                Text(stringResource(Res.string.btn_add_to_list))
            }
        },
    ) { paddingValues ->
        CreatureItem(
            creature = creature,
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        )
    }
}

@Preview
@Composable
private fun PreviewCreatureDetailScreenLight() {
    AppThemePreview(darkTheme = false) {
        CreatureDetailScreen(
            creature = SampleCreatureRepository.getFirst(),
            onAddToListClicked = {},
            onNavigateUpClicked = {},
        )
    }
}

@Preview
@Composable
private fun PreviewCreatureDetailScreenDark() {
    AppThemePreview(darkTheme = true) {
        CreatureDetailScreen(
            creature = SampleCreatureRepository.getFirst(),
            onAddToListClicked = {},
            onNavigateUpClicked = {},
        )
    }
}
