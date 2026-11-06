package com.cyrillrx.rpg.usercollection.presentation.component

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.core.presentation.formatRelativeTime
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import org.jetbrains.compose.resources.pluralStringResource
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.creature_count
import rpg_companion.app.shared.ui.generated.resources.magical_item_count
import rpg_companion.app.shared.ui.generated.resources.spell_count
import kotlin.time.Instant

@Composable
internal fun UserCollection.subtitle(updatedAt: Instant): String {
    val countItemText = formattedCount()
    val relativeTimeText = updatedAt.formatRelativeTime()
    return if (relativeTimeText == null) countItemText else "$countItemText - $relativeTimeText"
}

@Composable
private fun UserCollection.formattedCount(): String {
    val count = itemIds.size
    return pluralStringResource(
        resource = when (itemType) {
            UserCollection.ItemType.SPELL -> Res.plurals.spell_count
            UserCollection.ItemType.MAGICAL_ITEM -> Res.plurals.magical_item_count
            UserCollection.ItemType.MONSTER -> Res.plurals.creature_count
        },
        quantity = count,
        count,
    )
}
