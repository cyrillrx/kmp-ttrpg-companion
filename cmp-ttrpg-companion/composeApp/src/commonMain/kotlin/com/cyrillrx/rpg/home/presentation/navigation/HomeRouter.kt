package com.cyrillrx.rpg.home.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.rpg.campaign.navigation.CampaignRoute
import com.cyrillrx.rpg.character.presentation.navigation.PlayerCharacterRoute
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRoute
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRoute
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRoute

interface HomeRouter {
    fun openCampaignList() {}
    fun openCharacterSheets() {}

    fun openSpellBook() {}
    fun openAlternativeSpellBook() {}
    fun openMagicalItems() {}
    fun openBestiary() {}
}

class HomeRouterImpl(private val navController: NavController) : HomeRouter {
    override fun openCampaignList() {
        navController.navigate(CampaignRoute.List)
    }

    override fun openCharacterSheets() {
        navController.navigate(PlayerCharacterRoute.List)
    }

    override fun openSpellBook() {
        navController.navigate(SpellRoute.List)
    }

    override fun openAlternativeSpellBook() {
        navController.navigate(SpellRoute.AlternativeList)
    }

    override fun openMagicalItems() {
        navController.navigate(MagicalItemRoute.List)
    }

    override fun openBestiary() {
        navController.navigate(CreatureRoute.List)
    }
}
