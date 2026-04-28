package com.cyrillrx.rpg.magicalitem.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemListItem
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider
import org.jetbrains.compose.resources.StringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.empty_list_browse_magical_items

class MagicalItemItemProvider(
    private val onItemClicked: (String) -> Unit,
    override val onEmptyLayoutBtnClicked: () -> Unit = {},
) : ListItemProvider<MagicalItem> {

    override val emptyLayoutIcon: ImageVector = Icons.Outlined.Stars
    override val emptyLayoutBtnText: StringResource = Res.string.empty_list_browse_magical_items

    override fun getId(entity: MagicalItem): String = entity.id

    override fun getDisplayName(entity: MagicalItem, locale: String): String =
        entity.resolveTranslation(locale).name

    @Composable
    override fun ListItem(entity: MagicalItem, modifier: Modifier) {
        MagicalItemListItem(magicalItem = entity, onClick = { onItemClicked(entity.id) }, modifier = modifier)
    }
}
