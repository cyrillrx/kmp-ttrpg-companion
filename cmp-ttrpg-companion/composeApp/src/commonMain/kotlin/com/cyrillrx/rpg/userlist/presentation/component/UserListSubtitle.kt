package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.core.presentation.formatRelativeTime
import com.cyrillrx.rpg.userlist.domain.UserList
import org.jetbrains.compose.resources.pluralStringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.creature_count
import rpg_companion.composeapp.generated.resources.magical_item_count
import rpg_companion.composeapp.generated.resources.spell_count
import kotlin.time.Instant

@Composable
internal fun UserList.subtitle(updatedAt: Instant): String {
    val countItemText = formattedCount()
    val relativeTimeText = updatedAt.formatRelativeTime()
    return if (relativeTimeText == null) countItemText else "$countItemText - $relativeTimeText"
}

@Composable
private fun UserList.formattedCount(): String {
    val count = itemIds.size
    return pluralStringResource(
        resource = when (type) {
            UserList.Type.SPELL -> Res.plurals.spell_count
            UserList.Type.MAGICAL_ITEM -> Res.plurals.magical_item_count
            UserList.Type.MONSTER -> Res.plurals.creature_count
        },
        quantity = count,
        count,
    )
}
