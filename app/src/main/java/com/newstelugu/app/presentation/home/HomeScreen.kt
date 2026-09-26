package com.newstelugu.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newstelugu.app.core.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToArticle: (String) -> Unit,
    onNavigateToStock: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "🌐 NewsTelugu",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "English News → Telugu Translation",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                ShimmerLoader()
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerLoader()
            }
        } else if (state.error != null) {
            ErrorState(message = state.error!!, onRetry = { viewModel.loadHomeData() })
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Header Telugu Tagline Banner
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "తెలుగులో ప్రపంచ వార్తలు మరియు మార్కెట్ విశ్లేషణ",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Indian Markets Section
                item {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "🇮🇳 Indian Market Overview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.indianMarkets) { index ->
                                MarketCard(
                                    market = index,
                                    onClick = { onNavigateToStock(index.symbol) }
                                )
                            }
                        }
                    }
                }

                // Global Markets Section
                if (state.globalMarkets.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = "🌐 Global Markets",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(state.globalMarkets) { index ->
                                    MarketCard(
                                        market = index,
                                        onClick = { onNavigateToStock(index.symbol) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Breaking News Banner
                if (state.breakingNews.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🚨 BREAKING NEWS",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            NewsCard(
                                article = state.breakingNews.first(),
                                onClick = { onNavigateToArticle(state.breakingNews.first().id) }
                            )
                        }
                    }
                }

                // Financial Disclaimer Banner
                item {
                    FinancialDisclaimerBanner()
                }

                // Latest News Section
                item {
                    Text(
                        text = "📰 Latest News",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(state.latestNews) { article ->
                    NewsCard(
                        article = article,
                        onClick = { onNavigateToArticle(article.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                // Category News Feeds
                state.categoryNews.forEach { (catName, articles) ->
                    if (articles.isNotEmpty()) {
                        item {
                            Text(
                                text = "📌 $catName",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                            )
                        }
                        items(articles) { article ->
                            NewsCard(
                                article = article,
                                onClick = { onNavigateToArticle(article.id) },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
