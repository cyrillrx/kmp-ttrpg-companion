package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.dice.domain.DiceComposition
import com.cyrillrx.rpg.dice.domain.DiceCompositionRepository
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.MAX_DICE_COUNT
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.rollWithAdvantage
import com.cyrillrx.rpg.dice.domain.rollWithDisadvantage
import com.cyrillrx.rpg.dice.presentation.DiceRollerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.random.Random

class DiceRollerViewModel(
    private val compositionRepository: DiceCompositionRepository,
    private val random: Random = Random.Default,
) : ViewModel() {

    val state: StateFlow<DiceRollerState>
        field = MutableStateFlow(DiceRollerState())

    init {
        viewModelScope.launch {
            val saved = loadCompositionOrEmpty()
            restoreUntouchedComposition(saved)
            saveCompositionChanges(alreadySaved = saved)
        }
    }

    fun addDie(die: Die) {
        state.update { current ->
            if (current.diceCount() >= MAX_DICE_COUNT) return@update current

            current.copy(countByDie = current.countByDie + (die to (current.countByDie[die] ?: 0) + 1))
        }
    }

    fun removeDie(die: Die) {
        state.update { current ->
            val count = current.countByDie[die] ?: return@update current
            val countByDie = if (count > 1) current.countByDie + (die to count - 1) else current.countByDie - die
            current.copy(countByDie = countByDie)
        }
    }

    fun increaseModifier() {
        state.update { it.copy(modifier = (it.modifier + 1).coerceAtMost(MAX_MODIFIER)) }
    }

    fun decreaseModifier() {
        state.update { it.copy(modifier = (it.modifier - 1).coerceAtLeast(-MAX_MODIFIER)) }
    }

    fun roll() {
        state.update { current ->
            val composition = current.composition() ?: return@update current

            current.copy(lastRoll = composition.roll(random), rollCount = current.rollCount + 1)
        }
    }

    fun rollD20() {
        rollDirectly(Die.D20.roll(random))
    }

    fun rollD20WithAdvantage() {
        rollDirectly(Die.D20.rollWithAdvantage(random))
    }

    fun rollD20WithDisadvantage() {
        rollDirectly(Die.D20.rollWithDisadvantage(random))
    }

    fun clearComposition() {
        state.update { it.copy(countByDie = emptyMap()) }
    }

    private fun restoreUntouchedComposition(saved: DiceComposition) {
        state.update { current ->
            if (current.toDiceComposition() != DiceComposition()) return@update current

            current.copy(countByDie = saved.countByDie, modifier = saved.modifier)
        }
    }

    private suspend fun saveCompositionChanges(alreadySaved: DiceComposition) {
        state.map { it.toDiceComposition() }
            .distinctUntilChanged()
            .dropWhile { it == alreadySaved }
            .collect(::saveCompositionIgnoringFailure)
    }

    // Losing the composition only costs a few taps, so a storage failure never blocks the roller.
    private suspend fun loadCompositionOrEmpty(): DiceComposition = try {
        compositionRepository.load()
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        DiceComposition()
    }

    private suspend fun saveCompositionIgnoringFailure(composition: DiceComposition) {
        try {
            compositionRepository.save(composition)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // See loadCompositionOrEmpty.
        }
    }

    private fun rollDirectly(roll: DieRoll) {
        state.update { it.copy(lastRoll = RollPool(listOf(roll), it.modifier), rollCount = it.rollCount + 1) }
    }

    private fun DiceRollerState.toDiceComposition() = DiceComposition(countByDie, modifier)
}
