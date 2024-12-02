package com.cyrillrx.rpg.dnd.bestiary

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.bestiary.data.SampleBestiaryRepository
import com.cyrillrx.rpg.bestiary.presentation.AbilitiesLayout
import com.cyrillrx.rpg.bestiary.presentation.BestiaryScreen
import com.cyrillrx.rpg.common.theme.AppTheme

@Preview
@Composable
fun PreviewBestiaryScreen() {
    val creatures = SampleBestiaryRepository().getAll()
    AppTheme(darkTheme = false) {
        BestiaryScreen(creatures) { }
    }
}

@Preview
@Composable
fun PreviewAbilitiesLayout() {
    AppTheme(darkTheme = false) {
        AbilitiesLayout(14, 14, 14, 14, 14, 14)
    }
}
