package com.newstelugu.app.presentation.article

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.domain.model.NewsArticle
import com.newstelugu.app.domain.model.NewsSentiment
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailsScreen(
    articleId: String,
    onNavigateToStock: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var article by remember { mutableStateOf<NewsArticle?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(articleId) {
        scope.launch {
            try {
                val dto = NetworkClient.apiService.getArticleDetails(articleId)
                article = NewsArticle(
                    id = dto.id,
                    title = dto.title,
                    description = dto.description,
                    content = dto.content,
                    sourceName = dto.sourceName,
                    sourceUrl = dto.sourceUrl,
                    imageUrl = dto.imageUrl,
                    category = dto.category,
                    publishedAt = dto.publishedAt,
                    isBreaking = dto.isBreaking,
                    teluguTitle = dto.teluguTitle ?: dto.title,
                    teluguDescription = dto.teluguDescription ?: dto.description,
                    teluguSummary = dto.teluguSummary ?: dto.description,
                    keyFacts = dto.keyFacts,
                    whyItMatters = dto.whyItMatters,
                    marketImpact = dto.marketImpact,
                    marketImpactReason = dto.marketImpactReason,
                    sentiment = dto.sentiment?.let {
                        NewsSentiment(
                            sentiment = it.sentiment,
                            score = it.score,
                            marketRelevance = it.marketRelevance,
                            relatedCompany = it.relatedCompany,
                            relatedSymbol = it.relatedSymbol,
                            relatedSector = it.relatedSector
                        )
                    }
                )
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error loading article details"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News Details & Analysis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null || article == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(errorMessage ?: "Article not found", color = MaterialTheme.colorScheme.error)
            }
        } else {
            val art = article!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category & Sentiment
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = art.category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    art.sentiment?.let {
                        val isPos = it.sentiment == "POSITIVE"
                        val badgeCol = if (isPos) Color(0xFF2E7D32) else Color(0xFFC62828)
                        Text(
                            text = "${if (isPos) "🟢" else "🔴"} ${it.sentiment} (Score: ${it.score})",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = badgeCol
                        )
                    }
                }

                // Telugu Headline
                Text(
                    text = art.teluguTitle ?: art.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // English Subtitle
                Text(
                    text = "English: ${art.title}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                art.imageUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = art.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Summary Section
                DetailSectionCard("తెలుగు సారాంశం (Telugu Summary)") {
                    Text(text = art.teluguSummary ?: art.description ?: "", style = MaterialTheme.typography.bodyMedium)
                }

                // Key Facts Section
                if (art.keyFacts.isNotEmpty()) {
                    DetailSectionCard("ముఖ్యమైన అంశాలు (Key Information)") {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            art.keyFacts.forEach { fact ->
                                Text(text = "• $fact", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                // Why It Matters Section
                art.whyItMatters?.let { why ->
                    DetailSectionCard("ఎందుకు ఇది ముఖ్యం? (Why It Matters)") {
                        Text(text = why, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Market Impact Section
                DetailSectionCard("మార్కెట్ ప్రభావం (Market Impact)") {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Impact: ${art.marketImpact ?: "Positive"}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        art.marketImpactReason?.let { reason ->
                            Text(text = reason, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                // Related Stocks & Sector
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("సంబంధిత స్టాక్స్ & సెక్టార్ (Related Stocks)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        val symbol = art.sentiment?.relatedSymbol ?: "RELIANCE"
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.clickable { onNavigateToStock(symbol) }
                            ) {
                                Text(
                                    text = symbol,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Sector: ${art.sentiment?.relatedSector ?: "Banking / Markets"}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                // Attribution & Original Source Link
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Source: ${art.sourceName}", style = MaterialTheme.typography.titleSmall)
                        Text(text = "Published: Today", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Summary generated by NewsTelugu analysis engine.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                // Read Original Source Button at the bottom
                Button(
                    onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(art.sourceUrl))
                        context.startActivity(browserIntent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("READ ORIGINAL SOURCE")
                }
            }
        }
    }
}

@Composable
fun DetailSectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
