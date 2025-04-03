package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.creature.domain.BaseCreature

interface CreatureRouter {
    fun navigateUp()
    fun openCreatureDetail(creature: BaseCreature)
}

class CreatureRouterImpl(private val navController: NavController) : CreatureRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openCreatureDetail(creature: BaseCreature) {
        navController.navigate(CreatureRoute.Detail(creature.serialize()))
    }
}
