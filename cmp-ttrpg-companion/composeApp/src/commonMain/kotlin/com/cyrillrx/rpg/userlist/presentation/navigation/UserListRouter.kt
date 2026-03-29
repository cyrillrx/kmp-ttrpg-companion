package com.cyrillrx.rpg.userlist.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.rpg.userlist.domain.UserList

interface UserListRouter {
    fun navigateUp()
    fun openUserList(list: UserList) {}
}

class UserListRouterImpl(private val navController: NavController) : UserListRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }
}
