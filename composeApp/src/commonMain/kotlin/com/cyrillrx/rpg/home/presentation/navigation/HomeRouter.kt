package com.cyrillrx.rpg.home.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.rpg.app.Route
import com.cyrillrx.rpg.campaign.presentation.navigation.CampaignRoute
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
        navController.navigate(Route.CharacterSheetList)
    }

    override fun openSpellBook() {
        navController.navigate(SpellRoute.List)
    }

    override fun openAlternativeSpellBook() {
        navController.navigate(SpellRoute.AlternativeList)
    }

    override fun openMagicalItems() {
        navController.navigate(Route.MagicalItems)
    }

    override fun openBestiary() {
        navController.navigate(Route.Bestiary)
    }
}
