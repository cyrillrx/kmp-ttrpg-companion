package com.cyrillrx.rpg.dnd.bestiary

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.creature.data.SampleBestiaryRepository
import com.cyrillrx.rpg.creature.presentation.BestiaryState
import com.cyrillrx.rpg.creature.presentation.component.AbilitiesLayout
import com.cyrillrx.rpg.creature.presentation.component.BestiaryScreen
import com.cyrillrx.rpg.core.presentation.theme.AppTheme

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
fun PreviewBestiaryScreen() {
    val creatures = SampleBestiaryRepository().getAll()
    val state = BestiaryState.WithData("", creatures)
    AppTheme(darkTheme = false) {
        BestiaryScreen(state) {}
    }
}
