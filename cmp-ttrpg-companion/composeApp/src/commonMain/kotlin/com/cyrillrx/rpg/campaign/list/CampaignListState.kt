package com.cyrillrx.rpg.campaign.list

import com.cyrillrx.rpg.campaign.domain.Campaign
import org.jetbrains.compose.resources.StringResource

data class CampaignListState(
    val searchQuery: String,
    val body: Body,
) {
    sealed interface Body {
        data object Loading : Body

        data object Empty : Body

        data class Error(val errorMessage: StringResource) : Body

        data class WithData(val searchResults: List<Campaign>) : Body
    }
}
