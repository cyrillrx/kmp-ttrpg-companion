package com.cyrillrx.rpg.campaign.list.viewmodel

import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.campaign.list.CampaignListState
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
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CampaignListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = TestCampaignRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(repo: CampaignRepository = repository) = CampaignListViewModel(repo)

    @Test
    fun `initial state is Loading before coroutines run`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<CampaignListState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `state is Error when repository throws`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(FailingCampaignRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CampaignListState.Body.Error>(viewModel.state.value.body)
    }

    @Test
    fun `initial state is Empty when no campaigns exist`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CampaignListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `initial state is WithData when campaigns exist`() = runTest(testDispatcher) {
        repository.save(Campaign("1", "Campaign 1", RuleSet.DND5E))

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        val body = assertIs<CampaignListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 1, actual = body.searchResults.size)
    }

    @Test
    fun `silentRefresh updates state with fresh data without showing Loading`() = runTest(testDispatcher) {
        repository.save(Campaign("1", "Campaign 1", RuleSet.DND5E))

        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        repository.save(Campaign("2", "Campaign 2", RuleSet.PATHFINDER_2E))

        val emittedBodies = mutableListOf<CampaignListState.Body>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect { emittedBodies.add(it.body) }
        }

        viewModel.silentRefresh()
        advanceUntilIdle()

        assertTrue(emittedBodies.none { it is CampaignListState.Body.Loading })
        val body = assertIs<CampaignListState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = 2, actual = body.searchResults.size)
    }

    @Test
    fun `silentRefresh does nothing when state is already Loading`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        assertIs<CampaignListState.Body.Loading>(viewModel.state.value.body)

        viewModel.silentRefresh()

        assertIs<CampaignListState.Body.Loading>(viewModel.state.value.body)
    }

    @Test
    fun `silentRefresh keeps existing state when repository throws`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(FailsOnSecondCallCampaignRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        assertIs<CampaignListState.Body.Empty>(viewModel.state.value.body)

        viewModel.silentRefresh()
        advanceUntilIdle()

        assertIs<CampaignListState.Body.Empty>(viewModel.state.value.body)
    }

    @Test
    fun `filterByQuery updates searchQuery in state`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        advanceUntilIdle()

        viewModel.filterByQuery("dnd")
        advanceUntilIdle()

        assertEquals(expected = "dnd", actual = viewModel.state.value.searchQuery)
    }

}

private class TestCampaignRepository : CampaignRepository {
    private val campaigns = mutableListOf<Campaign>()

    override suspend fun getAll(filter: CampaignFilter?): List<Campaign> {
        filter ?: return campaigns
        val query = filter.query
        return if (query.isBlank()) campaigns else campaigns.filter { it.name.contains(query, ignoreCase = true) }
    }

    override suspend fun get(id: String): Campaign? = campaigns.find { it.id == id }
    override suspend fun save(campaign: Campaign) { campaigns.add(campaign) }
    override suspend fun delete(id: String) { campaigns.removeAll { it.id == id } }
}

private class FailingCampaignRepository : CampaignRepository {
    override suspend fun getAll(filter: CampaignFilter?): List<Campaign> = error("Repository error")
    override suspend fun get(id: String): Campaign? = error("Repository error")
    override suspend fun save(campaign: Campaign) = error("Repository error")
    override suspend fun delete(id: String) = error("Repository error")
}

private class FailsOnSecondCallCampaignRepository : CampaignRepository {
    private var callCount = 0

    override suspend fun getAll(filter: CampaignFilter?): List<Campaign> {
        if (callCount++ == 0) return emptyList()
        error("Repository error")
    }

    override suspend fun get(id: String): Campaign? = null
    override suspend fun save(campaign: Campaign) = Unit
    override suspend fun delete(id: String) = Unit
}
