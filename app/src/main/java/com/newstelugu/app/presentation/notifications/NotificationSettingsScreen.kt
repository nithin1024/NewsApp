package com.newstelugu.app.presentation.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚙️ Notification Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Categorized Notifications", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            SettingSwitchRow("🟢 Positive News Alerts", state.notifyPositive) { viewModel.toggleSetting("POSITIVE", it) }
            SettingSwitchRow("🔴 Negative News Alerts", state.notifyNegative) { viewModel.toggleSetting("NEGATIVE", it) }
            SettingSwitchRow("⚪ Neutral News Alerts", state.notifyNeutral) { viewModel.toggleSetting("NEUTRAL", it) }
            SettingSwitchRow("🚨 Breaking News Alerts", state.notifyBreaking) { viewModel.toggleSetting("BREAKING", it) }
            SettingSwitchRow("⭐ Watchlist News Alerts", state.notifyWatchlist) { viewModel.toggleSetting("WATCHLIST", it) }
            SettingSwitchRow("🏦 Market Overview Alerts", state.notifyMarket) { viewModel.toggleSetting("MARKET", it) }
            SettingSwitchRow("📊 Technical Indicator Alerts", state.notifyTechnical) { viewModel.toggleSetting("TECHNICAL", it) }
            SettingSwitchRow("📈 Price Alerts", state.notifyPrice) { viewModel.toggleSetting("PRICE", it) }
        }
    }
}

@Composable
fun SettingSwitchRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
