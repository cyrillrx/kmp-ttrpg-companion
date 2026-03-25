package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_spell_not_found

@Composable
fun SpellCardScreen(
    viewModel: SpellDetailViewModel,
    onNavigateUpClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_spell_not_found, s.id))
        is DetailState.Found -> SpellCardScreen(s.item, onNavigateUpClicked)
    }
}

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
