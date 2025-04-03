package com.cyrillrx.rpg.campaign.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.campaign.common.LocalizedRuleSet
import com.cyrillrx.rpg.campaign.common.getMessage
import com.cyrillrx.rpg.campaign.common.getName
import com.cyrillrx.rpg.campaign.create.viewmodel.CreateCampaignViewModel
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_create_campaign
import rpg_companion.composeapp.generated.resources.label_campaign_name
import rpg_companion.composeapp.generated.resources.label_campaign_rule_set
import rpg_companion.composeapp.generated.resources.ruleset_other
import rpg_companion.composeapp.generated.resources.title_create_campaign

@Composable
fun CreateCampaignScreen(viewModel: CreateCampaignViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreateCampaignScreen(
        state = state,
        onNavigateUpClicked = viewModel::onNavigateUpClicked,
        onCampaignNameChanged = viewModel::onCampaignNameChanged,
        onRuleSetSelected = viewModel::onRuleSetSelected,
        onCreateButtonClicked = viewModel::onCreateCampaignClicked,
        clearError = viewModel::clearError,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateCampaignScreen(
    state: CreateCampaignState,
    onNavigateUpClicked: () -> Unit,
    onCampaignNameChanged: (String) -> Unit,
    onRuleSetSelected: (RuleSet) -> Unit,
    onCreateButtonClicked: () -> Unit,
    clearError: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val errorMessage = state.error?.getMessage()?.let { stringResource(it) }
    if (errorMessage != null) {
        coroutineScope.launch {
            clearError()
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
    Scaffold(
        topBar = {
            SimpleTopBar(
                titleResource = Res.string.title_create_campaign,
                navigateUp = onNavigateUpClicked,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        val localizedRuleSets = RuleSet.entries
            .mapNotNull {
                if (it == RuleSet.UNDEFINED || it == RuleSet.OTHER) return@mapNotNull null
                LocalizedRuleSet(it, stringResource(it.getName()))
            }
            .sortedBy { it.localizedName } + LocalizedRuleSet(
            RuleSet.OTHER, stringResource(Res.string.ruleset_other),
        )

        Column(
            Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = spacingCommon,
                start = spacingCommon,
                end = spacingCommon,
            ),
        ) {
            OutlinedTextField(
                label = { Text(text = stringResource(Res.string.label_campaign_name)) },
                value = state.campaignName,
                onValueChange = onCampaignNameChanged,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.label_campaign_rule_set),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                )

                ChooseRuleSetButton(
                    ruleSets = localizedRuleSets,
                    selectedRuleSet = state.selectedRuleSet,
                    onRuleSetSelected = onRuleSetSelected,
                    modifier = Modifier.padding(spacingMedium),
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier = Modifier.fillMaxWidth(),
            ) {
                localizedRuleSets.forEach { ruleSet ->
                    InputChip(
                        onClick = { onRuleSetSelected(ruleSet.value) },
                        label = { Text(text = ruleSet.localizedName) },
                        selected = state.selectedRuleSet == ruleSet.value,
                    )
                }
            }

            Button(
                onClick = onCreateButtonClicked,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(text = stringResource(Res.string.btn_create_campaign))
            }
        }
    }
}

@Composable
fun ChooseRuleSetButton(
    ruleSets: List<LocalizedRuleSet>,
    selectedRuleSet: RuleSet,
    onRuleSetSelected: (RuleSet) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu: Boolean by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        InputChip(
            onClick = { showMenu = !showMenu },
            label = { Text(text = stringResource(selectedRuleSet.getName())) },
            selected = selectedRuleSet != RuleSet.UNDEFINED,
        )

        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            ruleSets.forEach { ruleSet ->
                if (ruleSet.value == RuleSet.UNDEFINED) return@forEach
                DropdownMenuItem(
                    onClick = {
                        onRuleSetSelected(ruleSet.value)
                        showMenu = false
                    },
                    text = { Text(text = ruleSet.localizedName) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateCampaignScreenLight() {
    val state = CreateCampaignState("Oblivion", RuleSet.DND5E, null)
    AppThemePreview(darkTheme = false) {
        CreateCampaignScreen(state, {}, {}, {}, {}, {})
    }
}

@Preview
@Composable
private fun PreviewCreateCampaignScreenDark() {
    val state = CreateCampaignState("Oblivion", RuleSet.DND5E, null)
    AppThemePreview(darkTheme = true) {
        CreateCampaignScreen(state, {}, {}, {}, {}, {})
    }
}
