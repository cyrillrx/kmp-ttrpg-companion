package com.cyrillrx.rpg.campaign.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.campaign.create.CreateCampaignState
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.campaign.navigation.CampaignRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateCampaignViewModel(
    private val router: CampaignRouter,
    private val repository: CampaignRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<CreateCampaignState> = MutableStateFlow(
        CreateCampaignState(campaignName = "", selectedRuleSet = RuleSet.UNDEFINED, error = null),
    )
    val state: StateFlow<CreateCampaignState> = _state.asStateFlow()

    fun navigateUp() {
        router.navigateUp()
    }

    fun onCampaignNameChanged(name: String) {
        _state.update { _state.value.copy(campaignName = name) }
    }

    fun onRuleSetSelected(ruleSet: RuleSet) {
        _state.update { _state.value.copy(selectedRuleSet = ruleSet) }
    }

    fun onCreateCampaignClicked() {
        val state = _state.value
        val campaignName = state.campaignName.trim()
        val selectedRuleSet = state.selectedRuleSet

        if (campaignName.isBlank()) {
            _state.update { _state.value.copy(error = CreateCampaignError.EmptyCampaignName) }
            return
        }

        if (selectedRuleSet == RuleSet.UNDEFINED) {
            _state.update { _state.value.copy(error = CreateCampaignError.UndefinedRuleSet) }
            return
        }

        val newCampaign = Campaign(
            id = campaignName,
            name = campaignName,
            ruleSet = selectedRuleSet,
        )

        viewModelScope.launch {
            val campaignAlreadyExists = repository.get(newCampaign.id) != null
            if (campaignAlreadyExists) {
                _state.update { _state.value.copy(error = CreateCampaignError.CampaignAlreadyExists) }
                return@launch
            }

            repository.save(newCampaign)
            navigateUp()
        }
    }

    fun clearError() {
        _state.update { _state.value.copy(error = null) }
    }
}
