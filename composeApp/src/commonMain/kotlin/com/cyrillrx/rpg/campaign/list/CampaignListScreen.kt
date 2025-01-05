package com.cyrillrx.rpg.campaign.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.list.viewmodel.CampaignListViewModel
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBar
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_campaign
import rpg_companion.composeapp.generated.resources.title_campaign_list

@Composable
fun CampaignListScreen(viewModel: CampaignListViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CampaignListScreen(
        state = state,
        navigateUp = viewModel::navigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCampaignClicked = viewModel::onCampaignClicked,
        onCreateCampaignClicked = viewModel::onCreateCampaignClicked,
    )
}

@Composable
fun CampaignListScreen(
    state: CampaignListState,
    navigateUp: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCampaignClicked: (Campaign) -> Unit,
    onCreateCampaignClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            Column {
                SimpleTopBar(
                    title = stringResource(Res.string.title_campaign_list),
                    navigateUp = navigateUp,
                )
                SearchBar(
                    hint = stringResource(Res.string.hint_search_campaign),
                    query = state.searchQuery,
                    onQueryChanged = onSearchQueryChanged,
                )
            }
        },
        floatingActionButton = {
            IconButton(
                onClick = onCreateCampaignClicked,
                modifier = Modifier
                    .padding(spacingCommon)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Create campaign",
                )
            }
        },
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            when (val body = state.body) {
                is CampaignListState.Body.Loading -> Loader()
                is CampaignListState.Body.Empty -> EmptySearch(state.searchQuery)
                is CampaignListState.Body.Error -> ErrorLayout(body.errorMessage)
                is CampaignListState.Body.WithData -> CampaignItemList(
                    campaigns = body.searchResults,
                    onCampaignClicked = onCampaignClicked,
                )
            }
        }
    }
}

@Composable
private fun CampaignItemList(
    campaigns: List<Campaign>,
    onCampaignClicked: (Campaign) -> Unit,
) {
    val searchResultsListState = rememberLazyListState()
    LaunchedEffect(campaigns) {
        searchResultsListState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = searchResultsListState,
    ) {
        items(campaigns) { campaign ->
            CampaignItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onCampaignClicked(campaign) },
                campaign = campaign,
            )
        }
    }
}

@Composable
private fun CampaignItem(
    modifier: Modifier,
    campaign: Campaign,
) {
    Row(modifier = modifier) {
//        Icon(
//            bitmap = imageResource(campaign.getIcon()),
//            contentDescription = campaign.name,
//            modifier = Modifier.padding(spacingCommon),
//        )
        Text(
            text = campaign.name,
            modifier = Modifier.padding(spacingCommon),
        )
    }
}
