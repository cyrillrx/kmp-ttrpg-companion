package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.cyrillrx.rpg.character.data.RamCharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterEditViewModelFactoryTest {

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
    fun `factory hands the import adjustment count to the view model`() {
        val factory = CharacterEditViewModelFactory("any-id", RamCharacterRepository(), adjustmentCount = 3)

        val viewModel = ViewModelProvider.create(store, factory)[CharacterEditViewModel::class]

        assertEquals(3, viewModel.pendingAdjustmentCount.value)
    }
}
