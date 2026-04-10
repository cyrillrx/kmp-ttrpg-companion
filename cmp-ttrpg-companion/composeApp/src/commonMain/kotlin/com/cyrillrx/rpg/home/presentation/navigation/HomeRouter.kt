package com.cyrillrx.rpg.home.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.campaign.navigation.CampaignRoute
import com.cyrillrx.rpg.character.presentation.navigation.PlayerCharacterRoute
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRoute
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRoute
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRoute

interface HomeRouter {
    fun openCampaignList() {}
    fun openCharacterSheetList() {}
    fun openSpellList() {}
    fun openSpellCardCarousel() {}
    fun openMagicalItemList() {}
    fun openMagicalItemCardCarousel() {}
    fun openCreatureCompactList() {}
    fun openCreatureList() {}
    fun openMySpellLists() {}
    fun openMyMagicalItemLists() {}
    fun openMyCreatureLists() {}
}

class HomeRouterImpl(private val backStack: NavBackStack<NavKey>) : HomeRouter {
    override fun openCampaignList() {
        backStack.add(CampaignRoute.List)
    }

    override fun openCharacterSheetList() {
        backStack.add(PlayerCharacterRoute.List)
    }

    override fun openSpellList() {
        backStack.add(SpellRoute.List)
    }

    override fun openSpellCardCarousel() {
        backStack.add(SpellRoute.CardCarousel)
    }

    override fun openMagicalItemList() {
        backStack.add(MagicalItemRoute.List)
    }

    override fun openMagicalItemCardCarousel() {
        backStack.add(MagicalItemRoute.CardCarousel)
    }

    override fun openCreatureCompactList() {
        backStack.add(CreatureRoute.CompactList)
    }

    override fun openCreatureList() {
        backStack.add(CreatureRoute.List)
    }

    override fun openMySpellLists() {
        backStack.add(SpellRoute.UserLists)
    }

    override fun openMyMagicalItemLists() {
        backStack.add(MagicalItemRoute.UserLists)
    }

    override fun openMyCreatureLists() {
        backStack.add(CreatureRoute.UserLists)
    }
}
