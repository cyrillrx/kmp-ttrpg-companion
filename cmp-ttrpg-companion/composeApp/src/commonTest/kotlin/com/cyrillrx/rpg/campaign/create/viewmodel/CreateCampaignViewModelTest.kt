package com.cyrillrx.rpg.campaign.create.viewmodel

import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.domain.RuleSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.launch
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CreateCampaignViewModelTest {

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

    private fun buildViewModel(repo: CampaignRepository = repository) = CreateCampaignViewModel(repo)

    @Test
    fun `onCreateCampaignClicked sets EmptyCampaignName error when name is blank`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()
        viewModel.onRuleSetSelected(RuleSet.DND5E)

        var navigationEmitted = false
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.navigationEvents.collect { navigationEmitted = true }
        }

        viewModel.onCreateCampaignClicked()

        assertIs<CreateCampaignError.EmptyCampaignName>(viewModel.state.value.error)
        assertTrue(!navigationEmitted)
    }

    @Test
    fun `onCreateCampaignClicked sets UndefinedRuleSet error when rule set is undefined`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()
        viewModel.onCampaignNameChanged("My Campaign")

        var navigationEmitted = false
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.navigationEvents.collect { navigationEmitted = true }
        }

        viewModel.onCreateCampaignClicked()

        assertIs<CreateCampaignError.UndefinedRuleSet>(viewModel.state.value.error)
        assertTrue(!navigationEmitted)
    }

    @Test
    fun `onCreateCampaignClicked sets CampaignAlreadyExists error when campaign exists`() = runTest(testDispatcher) {
        repository.save(Campaign("My Campaign", "My Campaign", RuleSet.DND5E))
        val viewModel = buildViewModel()
        viewModel.onCampaignNameChanged("My Campaign")
        viewModel.onRuleSetSelected(RuleSet.DND5E)

        var navigationEmitted = false
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.navigationEvents.collect { navigationEmitted = true }
        }

        viewModel.onCreateCampaignClicked()
        advanceUntilIdle()

        assertIs<CreateCampaignError.CampaignAlreadyExists>(viewModel.state.value.error)
        assertTrue(!navigationEmitted)
    }

    @Test
    fun `onCreateCampaignClicked saves campaign and emits NavigateUp event`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()
        viewModel.onCampaignNameChanged("New Campaign")
        viewModel.onRuleSetSelected(RuleSet.DND5E)

        var navigationEmitted = false
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.navigationEvents.collect { navigationEmitted = true }
        }

        viewModel.onCreateCampaignClicked()
        advanceUntilIdle()

        assertTrue(navigationEmitted)
        assertNull(viewModel.state.value.error)
        assertEquals(1, repository.campaigns.size)
        assertEquals("New Campaign", repository.campaigns.first().name)
    }

    @Test
    fun `clearError removes current error from state`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()

        viewModel.onCreateCampaignClicked()

        assertIs<CreateCampaignError.EmptyCampaignName>(viewModel.state.value.error)

        viewModel.clearError()

        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `onCampaignNameChanged updates campaign name in state`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()
        viewModel.onCampaignNameChanged("Heroes of Might")
        assertEquals("Heroes of Might", viewModel.state.value.campaignName)
    }

    @Test
    fun `onRuleSetSelected updates selected rule set in state`() = runTest(testDispatcher) {
        val viewModel = buildViewModel()
        viewModel.onRuleSetSelected(RuleSet.PATHFINDER_2E)
        assertEquals(RuleSet.PATHFINDER_2E, viewModel.state.value.selectedRuleSet)
    }
}

private class TestCampaignRepository : CampaignRepository {
    val campaigns = mutableListOf<Campaign>()

    override suspend fun getAll(filter: CampaignFilter?): List<Campaign> = campaigns
    override suspend fun get(id: String): Campaign? = campaigns.find { it.id == id }
    override suspend fun save(campaign: Campaign) { campaigns.add(campaign) }
    override suspend fun delete(id: String) { campaigns.removeAll { it.id == id } }
}
