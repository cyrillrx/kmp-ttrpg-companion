package com.cyrillrx.rpg.userlist.presentation

import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.userlist.domain.UserList
import org.jetbrains.compose.resources.StringResource

data class AddToListState(
    val body: Body = Body.Loading,
) {
    data class SelectableUserList(val list: UserList, val alreadyAdded: Boolean)

    sealed interface Body {
        data object Loading : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(
            val spell: Spell,
            val lists: List<SelectableUserList>,
            val pendingSelection: Set<String>,
        ) : Body
    }
}
