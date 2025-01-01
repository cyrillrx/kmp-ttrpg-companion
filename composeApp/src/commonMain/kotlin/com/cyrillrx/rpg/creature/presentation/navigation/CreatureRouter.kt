package com.cyrillrx.rpg.creature.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.creature.domain.Creature

interface CreatureRouter {
    fun openCreatureDetail(creature: Creature)
}

class CreatureRouterImpl(private val navController: NavController) : CreatureRouter {
    override fun openCreatureDetail(creature: Creature) {
        navController.navigate(CreatureRoute.Detail(creature.serialize()))
    }
}
