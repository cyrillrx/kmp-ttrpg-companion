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
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class DiceHistoryViewModelFactoryTest {

    private val store = ViewModelStore()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        store.clear()
        Dispatchers.resetMain()
    }

    @Test
    fun `factory hands the history repository to the history`() = runTest {
        val history = RamRollHistoryRepository()
        history.record(RollPool(listOf(SimpleRoll(Die.D4, value = 2))))

        val viewModel = ViewModelProvider.create(
            store,
            DiceHistoryViewModelFactory(history),
        )[DiceHistoryViewModel::class]

        assertIs<DiceHistoryState.WithData>(viewModel.state.value)
    }
}
