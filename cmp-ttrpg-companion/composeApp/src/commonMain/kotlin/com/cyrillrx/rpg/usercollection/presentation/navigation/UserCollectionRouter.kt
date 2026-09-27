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
    fun openUserCollection(collection: UserCollection)
}

class UserCollectionRouterImpl(private val backStack: NavBackStack<NavKey>) : UserCollectionRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openUserCollection(collection: UserCollection) {
        when (collection.itemType) {
            UserCollection.ItemType.SPELL -> backStack.add(SpellRoute.UserCollectionDetail(collection.id))
            UserCollection.ItemType.MAGICAL_ITEM -> backStack.add(MagicalItemRoute.UserCollectionDetail(collection.id))
            UserCollection.ItemType.MONSTER -> backStack.add(MonsterRoute.UserCollectionDetail(collection.id))
        }
    }
}
