package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.MAX_DICE_COUNT
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.rollWithAdvantage
import com.cyrillrx.rpg.dice.domain.rollWithDisadvantage
import com.cyrillrx.rpg.dice.presentation.DiceRollerState
import com.cyrillrx.rpg.dice.presentation.RollRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.random.Random

class DiceRollerViewModel(
    private val historyRepository: RollHistoryRepository,
    private val random: Random = Random.Default,
) : ViewModel() {

    val state: StateFlow<DiceRollerState>
        field = MutableStateFlow(DiceRollerState())

    private val recorder = RollRecorder(historyRepository)

    init {
        viewModelScope.launch { loadHistoryIgnoringFailure() }

        viewModelScope.launch {
            historyRepository.rolls.collect { rolls -> state.update { it.withHistory(rolls) } }
        }
    }

    override fun onCleared() = recorder.close()

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
        val composition = state.value.composition() ?: return

        showAndRecord(composition.roll(random))
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

    private fun rollDirectly(roll: DieRoll) {
        showAndRecord(RollPool(listOf(roll), state.value.modifier))
    }

    private fun showAndRecord(pool: RollPool) {
        val shown = state.updateAndGet { it.copy(lastRoll = pool, rollCount = it.rollCount + 1, lastRollId = null) }
        recorder.record(pool) { id -> state.update { it.withLastRollRecorded(shown.rollCount, id) } }
    }

    private suspend fun loadHistoryIgnoringFailure() = try {
        historyRepository.load()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("WARNING: failed to load the dice roll history: $e")
    }
}
