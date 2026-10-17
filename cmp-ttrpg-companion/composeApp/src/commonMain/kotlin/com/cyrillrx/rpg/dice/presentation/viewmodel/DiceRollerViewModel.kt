package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.rollWithAdvantage
import com.cyrillrx.rpg.dice.domain.rollWithDisadvantage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class DiceRollerViewModel(private val random: Random = Random.Default) : ViewModel() {

    val pool: StateFlow<RollPool>
        field = MutableStateFlow(RollPool())

    fun roll(die: Die) {
        add(die.roll(random))
    }

    fun rollD20WithAdvantage() {
        add(Die.D20.rollWithAdvantage(random))
    }

    fun rollD20WithDisadvantage() {
        add(Die.D20.rollWithDisadvantage(random))
    }

    fun remove(index: Int) {
        pool.update { it.remove(index) }
    }

    fun increaseModifier() {
        pool.update { it.withModifier((it.modifier + 1).coerceAtMost(MAX_MODIFIER)) }
    }

    fun decreaseModifier() {
        pool.update { it.withModifier((it.modifier - 1).coerceAtLeast(-MAX_MODIFIER)) }
    }

    fun reset() {
        pool.value = RollPool()
    }

    private fun add(roll: DieRoll) {
        if (pool.value.isFull()) return

        pool.update { it.add(roll) }
    }
}
