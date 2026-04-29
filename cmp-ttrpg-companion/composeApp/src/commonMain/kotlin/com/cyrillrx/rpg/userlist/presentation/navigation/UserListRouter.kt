package com.cyrillrx.rpg.userlist.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRoute
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRoute
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRoute
import com.cyrillrx.rpg.userlist.domain.UserList

interface UserListRouter {
    fun navigateUp()
    fun openUserList(list: UserList)
}

class UserListRouterImpl(private val backStack: NavBackStack<NavKey>) : UserListRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openUserList(list: UserList) {
        when (list.type) {
            UserList.Type.SPELL -> backStack.add(SpellRoute.UserListDetail(list.id))
            UserList.Type.MAGICAL_ITEM -> backStack.add(MagicalItemRoute.UserListDetail(list.id))
            UserList.Type.MONSTER -> backStack.add(MonsterRoute.UserListDetail(list.id))
        }
    }
}
