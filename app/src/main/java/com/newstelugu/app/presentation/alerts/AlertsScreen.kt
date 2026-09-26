package com.newstelugu.app.presentation.alerts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newstelugu.app.core.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    onBack: () -> Unit,
    viewModel: AlertsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var symbol by remember { mutableStateOf("") }
    var targetPrice by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🚨 Price & Technical Alerts", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Create Alert")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.priceAlerts.isEmpty() && state.technicalAlerts.isEmpty()) {
            EmptyState("No active price or technical alerts. Tap + to set a new alert.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Text("📈 Price Alerts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                items(state.priceAlerts) { alert ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(alert.stockSymbol, fontWeight = FontWeight.Bold)
                                Text("Alert when price ${alert.condition} ₹${alert.targetPrice}", style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { viewModel.deleteAlert(alert.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)); Text("📊 Technical Indicator Alerts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                items(state.technicalAlerts) { alert ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("${alert.stockSymbol} (${alert.indicator})", fontWeight = FontWeight.Bold)
                                Text("Alert when ${alert.indicator} ${alert.condition} ${alert.value}", style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { viewModel.deleteAlert(alert.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Create Price Alert") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = symbol, onValueChange = { symbol = it }, label = { Text("Symbol (e.g. RELIANCE)") })
                        OutlinedTextField(value = targetPrice, onValueChange = { targetPrice = it }, label = { Text("Target Price (₹)") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val p = targetPrice.toDoubleOrNull() ?: 0.0
                        if (symbol.isNotBlank() && p > 0) {
                            viewModel.addPriceAlert(symbol.uppercase(), "ABOVE", p)
                            showDialog = false
                            symbol = ""
                            targetPrice = ""
                        }
                    }) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}
