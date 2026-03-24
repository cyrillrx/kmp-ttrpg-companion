package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CreatureDetailScreen(
    creature: Creature,
    onNavigateUpClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = creature.name,
                navigateUp = onNavigateUpClicked,
            )
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
        CreatureDetailScreen(creature = SampleCreatureRepository().get(), onNavigateUpClicked = {})
    }
}

@Preview
@Composable
private fun PreviewCreatureDetailScreenDark() {
    AppThemePreview(darkTheme = true) {
        CreatureDetailScreen(creature = SampleCreatureRepository().get(), onNavigateUpClicked = {})
    }
}
