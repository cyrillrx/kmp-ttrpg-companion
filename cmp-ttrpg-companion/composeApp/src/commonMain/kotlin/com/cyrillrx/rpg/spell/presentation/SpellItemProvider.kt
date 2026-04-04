package com.cyrillrx.rpg.spell.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.component.SpellListItem
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider
import org.jetbrains.compose.resources.StringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.empty_list_browse_spells

class SpellItemProvider(
    private val onItemClicked: (String) -> Unit,
    override val onEmptyLayoutBtnClicked: () -> Unit = {},
) : ListItemProvider<Spell> {

    override val emptyLayoutIcon: ImageVector = Icons.Outlined.MenuBook
    override val emptyLayoutBtnText: StringResource = Res.string.empty_list_browse_spells

    override fun getId(entity: Spell): String = entity.id

    override fun getDisplayName(entity: Spell): String = entity.title

    @Composable
    override fun ListItem(entity: Spell, modifier: Modifier) {
        SpellListItem(spell = entity, onClick = { onItemClicked(entity.id) }, modifier = modifier)
    }
}
