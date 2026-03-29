package com.cyrillrx.rpg.userlist.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRoute
import com.cyrillrx.rpg.userlist.domain.UserList

interface UserListRouter {
    fun navigateUp()
    fun openUserList(list: UserList) {}
}

class UserListRouterImpl(private val navController: NavController) : UserListRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openUserList(list: UserList) {
        when (list.type) {
            UserList.Type.SPELL -> navController.navigate(SpellRoute.UserListDetail(list.id))
            UserList.Type.MAGICAL_ITEM -> TODO()
            UserList.Type.CREATURE -> TODO()
        }
    }
}
