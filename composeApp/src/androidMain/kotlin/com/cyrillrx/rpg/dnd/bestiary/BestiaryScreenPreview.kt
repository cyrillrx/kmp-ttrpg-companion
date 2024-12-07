package com.cyrillrx.rpg.dnd.bestiary

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.bestiary.data.SampleBestiaryRepository
import com.cyrillrx.rpg.bestiary.presentation.AbilitiesLayout
import com.cyrillrx.rpg.bestiary.presentation.BestiaryScreen
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
    AppTheme(darkTheme = false) {
        BestiaryScreen(creatures)
    }
}
