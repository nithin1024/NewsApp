package com.newstelugu.app.presentation.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.domain.model.StockQuote
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockSearchScreen(
    onSelectStock: (String) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<StockQuote>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Stocks", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { newQ ->
                    query = newQ
                    if (newQ.isNotBlank()) {
                        isLoading = true
                        scope.launch {
                            try {
                                val dtos = NetworkClient.apiService.searchStocks(newQ)
                                results = dtos.map { dto ->
                                    StockQuote(
                                        symbol = dto.symbol,
                                        name = dto.name,
                                        exchange = dto.exchange,
                                        instrumentKey = dto.instrumentKey,
                                        currentPrice = dto.currentPrice,
                                        change = dto.change,
                                        percentChange = dto.percentChange,
                                        openPrice = dto.openPrice,
                                        highPrice = dto.highPrice,
                                        lowPrice = dto.lowPrice,
                                        prevClose = dto.prevClose,
                                        volume = dto.volume,
                                        fiftyTwoWeekHigh = dto.fiftyTwoWeekHigh,
                                        fiftyTwoWeekLow = dto.fiftyTwoWeekLow,
                                        sector = dto.sector,
                                        lastUpdated = dto.lastUpdated
                                    )
                                }
                            } catch (_: Exception) {}
                            finally { isLoading = false }
                        }
                    } else {
                        results = emptyList()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search TCS, INFY, RELIANCE...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(results) { stock ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectStock(stock.symbol) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(stock.symbol, fontWeight = FontWeight.Bold)
                                Text(stock.name, style = MaterialTheme.typography.bodySmall)
                            }
                            Column {
                                Text("₹${stock.currentPrice}", fontWeight = FontWeight.Bold)
                                Text(
                                    text = String.format("%+.2f%%", stock.percentChange),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (stock.change >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
