package com.cyrillrx.rpg.home.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.campaign.navigation.CampaignRoute
import com.cyrillrx.rpg.character.presentation.navigation.PlayerCharacterRoute
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRoute
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRoute
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRoute
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRoute

interface HomeRouter {
    fun openCampaignList() {}
    fun openCharacterSheetList() {}
    fun openSpellCompendium() {}
    fun openMagicalItemCompendium() {}
    fun openMonsterCompendium() {}
    fun openMySpellLists() {}
    fun openMyMagicalItemLists() {}
    fun openMyMonsterLists() {}
}

class HomeRouterImpl(private val backStack: NavBackStack<NavKey>) : HomeRouter {
    override fun openCampaignList() {
        backStack.add(CampaignRoute.List)
    }

    override fun openCharacterSheetList() {
        backStack.add(PlayerCharacterRoute.List)
    }

    override fun openSpellCompendium() {
        backStack.add(SpellRoute.Compendium)
    }

    override fun openMagicalItemCompendium() {
        backStack.add(MagicalItemRoute.Compendium)
    }

    override fun openMonsterCompendium() {
        backStack.add(MonsterRoute.Compendium)
    }

    override fun openMySpellLists() {
        backStack.add(UserListRoute.Spell)
    }

    override fun openMyMagicalItemLists() {
        backStack.add(UserListRoute.MagicalItem)
    }

    override fun openMyMonsterLists() {
        backStack.add(UserListRoute.Creature)
    }
}
