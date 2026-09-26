package com.newstelugu.app.presentation.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newstelugu.app.core.ui.components.EmptyState

val NOTIF_FILTERS = listOf("ALL", "POSITIVE", "NEGATIVE", "NEUTRAL", "BREAKING", "MARKET", "PRICE", "TECHNICAL")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationHistoryScreen(
    onNavigateToArticle: (String) -> Unit,
    onNavigateToStock: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val filteredList = remember(state.notifications, state.filterType) {
        if (state.filterType == "ALL") state.notifications
        else state.notifications.filter { it.type == state.filterType }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🔔 Notification History", fontWeight = FontWeight.Bold) },
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
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(NOTIF_FILTERS) { filter ->
                    FilterChip(
                        selected = (state.filterType == filter),
                        onClick = { viewModel.setFilterType(filter) },
                        label = { Text(filter) }
                    )
                }
            }

            if (filteredList.isEmpty()) {
                EmptyState("No notifications in history")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { notif ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (notif.articleId != null) onNavigateToArticle(notif.articleId)
                                    else if (notif.stockSymbol != null) onNavigateToStock(notif.stockSymbol)
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(notif.title, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(notif.message, style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(notif.createdAt, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
            }
        }
    }
}
