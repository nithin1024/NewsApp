package com.newstelugu.app.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationScreen(onBack: () -> Unit) {
    val categories = remember { mutableStateListOf("Markets", "Economy", "Technology", "Banking") }
    val companies = remember { mutableStateListOf("RELIANCE", "INFY", "TCS", "HDFCBANK") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⭐ Personalization", fontWeight = FontWeight.Bold) },
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
            Text("Favorite Categories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Markets", "Economy", "Technology", "Banking", "Stock Market", "Startups", "Sports").forEach { cat ->
                    FilterChip(
                        selected = categories.contains(cat),
                        onClick = {
                            if (categories.contains(cat)) categories.remove(cat) else categories.add(cat)
                        },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Favorite Companies & Stocks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("RELIANCE", "INFY", "TCS", "HDFCBANK", "ICICIBANK", "SBIN", "TATAMOTORS").forEach { sym ->
                    FilterChip(
                        selected = companies.contains(sym),
                        onClick = {
                            if (companies.contains(sym)) companies.remove(sym) else companies.add(sym)
                        },
                        label = { Text(sym) }
                    )
                }
            }
        }
    }
}
