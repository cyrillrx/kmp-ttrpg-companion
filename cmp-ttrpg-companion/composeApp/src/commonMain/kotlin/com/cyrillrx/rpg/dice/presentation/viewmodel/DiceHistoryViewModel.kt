package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.presentation.detachedCommitScope
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import com.cyrillrx.rpg.dice.presentation.groupByDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

class DiceHistoryViewModel(
    private val repository: RollHistoryRepository,
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) : ViewModel() {

    private val loading = MutableStateFlow(Loading.PENDING)

    /** The newest roll of each clear the undo snackbar can still cancel; the history hides up to the highest. */
    private val pendingClears = MutableStateFlow(emptySet<Long>())

    private val commitScope = detachedCommitScope()

    val state: StateFlow<DiceHistoryState> = combine(loading, repository.rolls, pendingClears) {
        loading,
        rolls,
        clears,
        ->
        val body = when (loading) {
            Loading.PENDING -> DiceHistoryState.Body.Loading
            Loading.FAILED -> DiceHistoryState.Body.Error
            Loading.DONE -> render(rolls, hiddenUpTo = clears.maxOrNull())
        }
        DiceHistoryState(body)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, DiceHistoryState())

    init {
        viewModelScope.launch { loading.value = load() }
    }

    override fun onCleared() {
        commitPendingClears()
    }

    fun commitPendingClears() {
        pendingClears.value.forEach(::commitClear)
    }

    /** Hides every roll on screen; returns what [undoClear] or [commitClear] then take, or null with nothing to clear. */
    fun clearOptimistically(): Long? {
        val newest =
            (state.value.body as? DiceHistoryState.Body.WithData)?.days?.first()?.rolls?.first()?.id ?: return null

        pendingClears.update { it + newest }
        return newest
    }

    fun undoClear(upTo: Long) {
        pendingClears.update { it - upTo }
    }

    fun commitClear(upTo: Long) {
        if (upTo !in pendingClears.value) return

        commitScope.launch {
            try {
                repository.clearUpTo(upTo)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                println("WARNING: failed to clear the dice roll history: $e")
            }
            pendingClears.update { it - upTo }
        }
    }

    private fun render(rolls: List<RecordedRoll>, hiddenUpTo: Long?): DiceHistoryState.Body {
        val visible = if (hiddenUpTo == null) rolls else rolls.filter { it.id > hiddenUpTo }
        if (visible.isEmpty()) return DiceHistoryState.Body.Empty

        return DiceHistoryState.Body.WithData(visible.groupByDay(timeZone), today = clock.todayIn(timeZone))
    }

    private suspend fun load(): Loading = try {
        repository.load()
        Loading.DONE
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("WARNING: failed to load the dice roll history: $e")
        Loading.FAILED
    }

    private enum class Loading { PENDING, DONE, FAILED }
}
