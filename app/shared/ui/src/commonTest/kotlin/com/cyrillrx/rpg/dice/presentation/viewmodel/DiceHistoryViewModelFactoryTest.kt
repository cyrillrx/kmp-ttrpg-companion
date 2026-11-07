package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.cyrillrx.rpg.dice.data.RamRollHistoryRepository
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class DiceHistoryViewModelFactoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val store = ViewModelStore()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        store.clear()
        Dispatchers.resetMain()
    }

    @Test
    fun `factory hands the history repository to the history`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        history.record(RollPool(listOf(SimpleRoll(Die.D4, value = 2))))

        val viewModel = ViewModelProvider.create(
            store,
            DiceHistoryViewModelFactory(history),
        )[DiceHistoryViewModel::class]
        advanceUntilIdle()

        assertIs<DiceHistoryState.Body.WithData>(viewModel.state.value.body)
    }
}
