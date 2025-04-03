package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SpellCardScreen(
    spell: Spell,
    onNavigateUpClicked: () -> Unit,
) {
    Scaffold { paddingValues ->
        SpellCard(
            spell = spell,
            modifier = Modifier
                .clickable { onNavigateUpClicked() }
                .padding(paddingValues),
        )
    }
}

@Preview
@Composable
fun PreviewSpellCardScreen() {
    val spell = SampleSpellRepository().get()
    SpellCardScreen(spell, {})
}
