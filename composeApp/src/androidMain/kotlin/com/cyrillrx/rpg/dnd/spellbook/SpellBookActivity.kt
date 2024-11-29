package com.cyrillrx.rpg.dnd.spellbook

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cyrillrx.rpg.Router.openSpellDetail
import com.cyrillrx.rpg.common.theme.AppTheme
import com.cyrillrx.rpg.spellbook.data.JsonSpellRepository
import com.cyrillrx.rpg.spellbook.presentation.SpellBookViewModel
import com.cyrillrx.rpg.spellbook.presentation.SpellBookScreen
import kotlinx.coroutines.launch

class SpellBookActivity : AppCompatActivity() {

    private val viewModel by viewModels<SpellBookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                SpellBookScreen(viewModel) { spell -> openSpellDetail(spell) }
            }
        }

        lifecycleScope.launch {
            val spells = JsonSpellRepository().getSpells()
            viewModel.init(spells)
        }
    }
}
