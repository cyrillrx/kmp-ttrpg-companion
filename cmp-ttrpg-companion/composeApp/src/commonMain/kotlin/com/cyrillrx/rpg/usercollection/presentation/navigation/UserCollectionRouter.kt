package com.cyrillrx.rpg.usercollection.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRoute
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRoute
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRoute
import com.cyrillrx.rpg.usercollection.domain.UserCollection

interface UserCollectionRouter {
    fun navigateUp()
    fun openUserCollection(list: UserCollection)
}

class UserCollectionRouterImpl(private val backStack: NavBackStack<NavKey>) : UserCollectionRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openUserCollection(list: UserCollection) {
        when (list.type) {
            UserCollection.Type.SPELL -> backStack.add(SpellRoute.UserCollectionDetail(list.id))
            UserCollection.Type.MAGICAL_ITEM -> backStack.add(MagicalItemRoute.UserCollectionDetail(list.id))
            UserCollection.Type.MONSTER -> backStack.add(MonsterRoute.UserCollectionDetail(list.id))
        }
    }
}
