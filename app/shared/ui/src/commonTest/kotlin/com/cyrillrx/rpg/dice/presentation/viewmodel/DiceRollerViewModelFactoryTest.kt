package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.cyrillrx.rpg.dice.data.RamRollHistoryRepository
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
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DiceRollerViewModelFactoryTest {

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
    fun `factory hands the history repository to the roller`() = runTest(testDispatcher) {
        val history = RamRollHistoryRepository()
        val factory = DiceRollerViewModelFactory(history)

        ViewModelProvider.create(store, factory)[DiceRollerViewModel::class].rollD20()
        advanceUntilIdle()

        assertEquals(1, history.rolls.value.size)
    }
}
