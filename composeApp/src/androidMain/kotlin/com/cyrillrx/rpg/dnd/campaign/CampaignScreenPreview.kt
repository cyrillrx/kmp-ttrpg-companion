package com.cyrillrx.rpg.dnd.campaign

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.campaign.create.CreateCampaignScreen
import com.cyrillrx.rpg.campaign.create.CreateCampaignState
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.campaign.presentation.CampaignListState
import com.cyrillrx.rpg.campaign.presentation.component.CampaignListScreen
import com.cyrillrx.rpg.core.presentation.theme.AppTheme

private val campaigns = mutableListOf<Campaign>().apply {
    add(Campaign("1", "Campaign 1", RuleSet.DND5E))
    add(Campaign("2", "Campaign 2", RuleSet.PATHFINDER_2E))
    add(Campaign("3", "Campaign 3", RuleSet.VAMPIRE_THE_MASQUERADE_5E))
}
private val stateWithSampleData = CampaignListState(
    searchQuery = "",
    body = CampaignListState.Body.WithData(campaigns),
)

@Preview
@Composable
private fun PreviewCampaignListScreenLight() {
    AppTheme(darkTheme = false) {
        CampaignListScreen(stateWithSampleData, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewCampaignListScreenDark() {
    AppTheme(darkTheme = true) {
        CampaignListScreen(stateWithSampleData, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewCreateCampaignScreenLight() {
    val state = CreateCampaignState("Oblivion", RuleSet.DND5E, null)
    AppTheme(darkTheme = false) {
        CreateCampaignScreen(state, {}, {}, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewCreateCampaignScreenDark() {
    val state = CreateCampaignState("Oblivion", RuleSet.DND5E, null)
    AppTheme(darkTheme = true) {
        CreateCampaignScreen(state, {}, {}, {}, {}, {})
    }
}
