package com.cyrillrx.rpg.core.presentation.viewmodel

import com.cyrillrx.rpg.core.presentation.ScrollPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BaseListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `savedScrollPosition defaults to ScrollPosition()`() = runTest(testDispatcher) {
        val viewModel = TestableBaseListViewModel()

        assertEquals(expected = ScrollPosition(), actual = viewModel.savedScrollPosition)
    }

    @Test
    fun `saveScrollPosition updates savedScrollPosition`() = runTest(testDispatcher) {
        val viewModel = TestableBaseListViewModel()
        val position = ScrollPosition(index = 10, offset = 50)

        viewModel.saveScrollPosition(position)

        assertEquals(expected = position, actual = viewModel.savedScrollPosition)
    }

    @Test
    fun `scrollToTop resets savedScrollPosition to default`() = runTest(testDispatcher) {
        val viewModel = TestableBaseListViewModel()
        viewModel.saveScrollPosition(ScrollPosition(index = 10, offset = 50))

        viewModel.triggerScrollToTop()

        assertEquals(expected = ScrollPosition(), actual = viewModel.savedScrollPosition)
    }

    @Test
    fun `scrollToTop emits on scrollToTopEvents`() = runTest(testDispatcher) {
        val viewModel = TestableBaseListViewModel()
        val events = mutableListOf<Unit>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.scrollToTopEvents.collect { events.add(it) }
        }

        viewModel.triggerScrollToTop()
        advanceUntilIdle()

        assertEquals(expected = 1, actual = events.size)
    }

    @Test
    fun `scrollToTop resets savedScrollPosition before emitting event`() = runTest(testDispatcher) {
        val viewModel = TestableBaseListViewModel()
        viewModel.saveScrollPosition(ScrollPosition(index = 5, offset = 20))
        var positionAtEmitTime: ScrollPosition? = null

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.scrollToTopEvents.collect {
                positionAtEmitTime = viewModel.savedScrollPosition
            }
        }

        viewModel.triggerScrollToTop()
        advanceUntilIdle()

        assertEquals(expected = ScrollPosition(), actual = positionAtEmitTime)
    }
}

private class TestableBaseListViewModel : BaseListViewModel() {
    fun triggerScrollToTop() = scrollToTop()
}
