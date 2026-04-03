package com.cyrillrx.rpg.magicalitem.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemListItem
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider

class MagicalItemItemProvider(
    private val onItemClicked: (String) -> Unit,
) : ListItemProvider<MagicalItem> {

    override fun getId(entity: MagicalItem): String = entity.id

    override fun getDisplayName(entity: MagicalItem): String = entity.title

    @Composable
    override fun ListItem(entity: MagicalItem, modifier: Modifier) {
        MagicalItemListItem(magicalItem = entity, onClick = { onItemClicked(entity.id) }, modifier = modifier)
    }
}
