package com.cyrillrx.rpg.campaign.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.list.CampaignListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_campaign
import kotlin.coroutines.cancellation.CancellationException

class CampaignListViewModel(
    private val repository: CampaignRepository,
) : ViewModel() {

    private var activeJob: Job? = null
    val state: StateFlow<CampaignListState>
        field = MutableStateFlow(CampaignListState(searchQuery = "", body = CampaignListState.Body.Loading))

    init {
        loadCampaigns(query = "")
    }

    fun filterByQuery(query: String) {
        activeJob?.cancel()
        activeJob = loadCampaigns(query)
    }

    fun silentRefresh() {
        if (state.value.body is CampaignListState.Body.Loading) return
        activeJob?.cancel()
        activeJob = refreshCampaigns()
    }

    private fun refreshCampaigns(): Job =
        viewModelScope.launch {
            try {
                fetchAndUpdateCampaigns(state.value.searchQuery)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Keep existing state on refresh failure
            }
        }

    private fun loadCampaigns(query: String): Job =
        viewModelScope.launch {
            state.update { CampaignListState(searchQuery = query, body = CampaignListState.Body.Loading) }
            try {
                fetchAndUpdateCampaigns(query)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = CampaignListState.Body.Error(errorMessage = Res.string.error_while_loading_campaign)) }
            }
        }

    private suspend fun fetchAndUpdateCampaigns(query: String) {
        val filter = CampaignFilter(query = query)
        val campaigns = repository.getAll(filter)
        val body = if (campaigns.isEmpty()) {
            CampaignListState.Body.Empty
        } else {
            CampaignListState.Body.WithData(campaigns)
        }
        state.update { it.copy(body = body) }
    }
}
