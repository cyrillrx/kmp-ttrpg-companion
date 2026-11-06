package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.presentation.detachedCommitScope
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import com.cyrillrx.rpg.dice.presentation.groupByDay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.getAndUpdate
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

    private val clears = MutableStateFlow(Clears())

    private val commitScope = detachedCommitScope()

    val state: StateFlow<DiceHistoryState> = combine(loading, repository.rolls, clears) {
        loading,
        rolls,
        clears,
        ->
        val body = when (loading) {
            Loading.PENDING -> DiceHistoryState.Body.Loading
            Loading.FAILED -> DiceHistoryState.Body.Error
            Loading.DONE -> render(rolls, clears.hiddenUpTo)
        }
        DiceHistoryState(body, clears.hiddenUpTo)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, DiceHistoryState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch { loading.value = load() }
    }

    override fun onCleared() {
        commitPendingClears()
    }

    fun commitPendingClears() {
        val claimed = clears.getAndUpdate { it.copy(undoable = emptySet(), committing = it.committing + it.undoable) }
        claimed.undoable.forEach(::commit)
    }

    /** Hides every roll on screen; returns what [undoClear] or [commitClear] then take, or null with nothing to clear. */
    fun clearOptimistically(): Long? {
        val newest =
            (state.value.body as? DiceHistoryState.Body.WithData)?.days?.first()?.rolls?.first()?.id ?: return null

        val hiddenUpTo = clears.value.hiddenUpTo
        if (hiddenUpTo != null && newest <= hiddenUpTo) return null

        clears.update { it.copy(undoable = it.undoable + newest) }
        return newest
    }

    fun undoClear(upTo: Long) {
        clears.update { it.copy(undoable = it.undoable - upTo) }
    }

    fun commitClear(upTo: Long) {
        val claimed = clears.getAndUpdate { it.claim(upTo) }
        if (upTo !in claimed.undoable) return

        commit(upTo)
    }

    private fun commit(upTo: Long) {
        commitScope.launch {
            val cleared = clear(upTo)
            clears.update { it.copy(committing = it.committing - upTo) }
            if (!cleared) events.emit(Event.ClearError)
        }
    }

    private suspend fun clear(upTo: Long): Boolean = try {
        repository.clearUpTo(upTo)
        true
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("WARNING: failed to clear the dice roll history: $e")
        false
    }

    private fun render(rolls: List<RecordedRoll>, hiddenUpTo: Long?): DiceHistoryState.Body {
        val visible = if (hiddenUpTo == null) rolls else rolls.filter { it.id > hiddenUpTo }
        if (visible.isEmpty()) return DiceHistoryState.Body.Empty

        // TODO(#368): refresh today when the date changes, not only when the rolls or the clears do
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

    sealed interface Event {
        data object ClearError : Event
    }

    private enum class Loading { PENDING, DONE, FAILED }

    /**
     * The newest roll of each clear: [undoable] while the undo snackbar can still cancel it, then [committing]
     * until the repository call ends, so the rolls stay hidden meanwhile and the clear runs once.
     */
    private data class Clears(val undoable: Set<Long> = emptySet(), val committing: Set<Long> = emptySet()) {
        val hiddenUpTo: Long?
            get() = (undoable + committing).maxOrNull()

        fun claim(upTo: Long): Clears {
            if (upTo !in undoable) return this

            return copy(undoable = undoable - upTo, committing = committing + upTo)
        }
    }
}
