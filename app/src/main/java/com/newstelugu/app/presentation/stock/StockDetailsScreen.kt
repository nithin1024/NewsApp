package com.newstelugu.app.presentation.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newstelugu.app.core.ui.components.FinancialDisclaimerBanner
import com.newstelugu.app.core.ui.components.StockChart

val CHART_PERIODS = listOf("1D", "1W", "1M", "3M", "6M", "1Y", "5Y")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailsScreen(
    symbol: String,
    onBack: () -> Unit,
    viewModel: StockViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.loadStockDetails(symbol)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(symbol, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null || state.quote == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(state.error ?: "Stock quote unavailable", color = MaterialTheme.colorScheme.error)
            }
        } else {
            val q = state.quote!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Price & Change Overview
                Text(text = q.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "₹${q.currentPrice}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = String.format("%+.2f (%+.2f%%)", q.change, q.percentChange),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (q.change >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Chart Period Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CHART_PERIODS.forEach { period ->
                        FilterChip(
                            selected = (state.period == period),
                            onClick = { viewModel.loadStockDetails(symbol, period) },
                            label = { Text(period) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Candle Line Chart
                StockChart(candles = state.candles)

                Spacer(modifier = Modifier.height(16.dp))

                // Key Statistics Table
                Text(text = "📊 Key Statistics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Open: ₹${q.openPrice}")
                            Text("High: ₹${q.highPrice}")
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Low: ₹${q.lowPrice}")
                            Text("Prev Close: ₹${q.prevClose}")
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Volume: ${q.volume}")
                            Text("52W High: ₹${q.fiftyTwoWeekHigh}")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Technical Indicators Section
                state.indicators?.let { ind ->
                    Text(text = "📈 Technical Indicators", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            IndicatorRow(ind.rsi14.name, "${ind.rsi14.value}", ind.rsi14.status)
                            IndicatorRow(ind.sma20.name, "₹${ind.sma20.value}", ind.sma20.status)
                            IndicatorRow(ind.sma50.name, "₹${ind.sma50.value}", ind.sma50.status)
                            IndicatorRow(ind.sma200.name, "₹${ind.sma200.value}", ind.sma200.status)
                            IndicatorRow(ind.ema20.name, "₹${ind.ema20.value}", ind.ema20.status)
                            IndicatorRow(ind.macd.name, "${ind.macd.value}", ind.macd.status)
                            IndicatorRow(ind.bollingerBands.name, "${ind.bollingerBands.value}", ind.bollingerBands.status)
                            IndicatorRow(ind.atr.name, "${ind.atr.value}", ind.atr.status)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                FinancialDisclaimerBanner()
            }
        }
    }
}

@Composable
fun IndicatorRow(name: String, value: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = name, fontWeight = FontWeight.Bold)
            Text(text = status, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
        Text(text = value, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}
