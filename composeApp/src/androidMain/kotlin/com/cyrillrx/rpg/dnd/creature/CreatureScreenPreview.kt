package com.cyrillrx.rpg.dnd.creature

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.presentation.CreatureListState
import com.cyrillrx.rpg.creature.presentation.component.AbilitiesLayout
import com.cyrillrx.rpg.creature.presentation.component.CreatureListScreen

@Preview
@Composable
fun PreviewAbilitiesLayout() {
    AppTheme(darkTheme = false) {
        AbilitiesLayout(
            str = "10 (+0)",
            dex = "16 (+3)",
            con = "12 (+1)",
            int = "10 (+0)",
            wis = "18 (+4)",
            cha = "10 (+0)",
        )
    }
}

@Preview
@Composable
fun PreviewCreatureListScreen() {
    val creatures = SampleCreatureRepository().getAll()
    val state = CreatureListState(
        searchQuery = "",
        body = CreatureListState.Body.WithData(creatures),
    )
    AppTheme(darkTheme = false) {
        CreatureListScreen(state, {}, {})
    }
}
