package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.cyrillrx.rpg.dice.data.RamDiceCompositionRepository
import com.cyrillrx.rpg.dice.domain.DiceComposition
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.d4
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

    private val store = ViewModelStore()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        store.clear()
        Dispatchers.resetMain()
    }

    @Test
    fun `factory hands the composition repository to the view model`() = runTest {
        val repository = RamDiceCompositionRepository(DiceComposition(countByDie = mapOf(Die.D4 to 1)))

        val viewModel = ViewModelProvider.create(
            store,
            DiceRollerViewModelFactory(repository),
        )[DiceRollerViewModel::class]
        advanceUntilIdle()

        assertEquals(1.d4, viewModel.state.value.composition())
    }
}
