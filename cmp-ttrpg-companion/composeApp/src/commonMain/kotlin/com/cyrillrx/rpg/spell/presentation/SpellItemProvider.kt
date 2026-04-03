package com.cyrillrx.rpg.spell.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.component.SpellListItem
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider

class SpellItemProvider(
    private val onItemClicked: (String) -> Unit,
) : ListItemProvider<Spell> {

    override fun getId(entity: Spell): String = entity.id

    override fun getDisplayName(entity: Spell): String = entity.title

    @Composable
    override fun ListItem(entity: Spell, modifier: Modifier) {
        SpellListItem(spell = entity, onClick = { onItemClicked(entity.id) }, modifier = modifier)
    }
}
