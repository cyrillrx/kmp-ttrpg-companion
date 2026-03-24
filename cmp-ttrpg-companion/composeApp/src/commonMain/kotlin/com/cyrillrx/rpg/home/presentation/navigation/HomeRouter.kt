package com.cyrillrx.rpg.home.presentation.navigation

import androidx.navigation.NavController
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
    fun openCreatureDetailList() {}
}

class HomeRouterImpl(private val navController: NavController) : HomeRouter {
    override fun openCampaignList() {
        navController.navigate(CampaignRoute.List)
    }

    override fun openCharacterSheetList() {
        navController.navigate(PlayerCharacterRoute.List)
    }

    override fun openSpellList() {
        navController.navigate(SpellRoute.List)
    }

    override fun openSpellCardCarousel() {
        navController.navigate(SpellRoute.CardCarousel)
    }

    override fun openMagicalItemList() {
        navController.navigate(MagicalItemRoute.List)
    }

    override fun openMagicalItemCardCarousel() {
        navController.navigate(MagicalItemRoute.CardCarousel)
    }

    override fun openCreatureCompactList() {
        navController.navigate(CreatureRoute.CompactList)
    }

    override fun openCreatureDetailList() {
        navController.navigate(CreatureRoute.DetailList)
    }
}
