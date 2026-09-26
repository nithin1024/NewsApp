package com.newstelugu.app.presentation.article

import android.content.Intent
import android.net.Uri
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
                title = { Text("Article Details", fontWeight = FontWeight.Bold) },
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
                    .padding(16.dp)
            ) {
                // Category & Sentiment Badges
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
                            text = art.category.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    art.sentiment?.let {
                        Text(
                            text = "${it.sentiment} (Score: ${it.score})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (it.sentiment == "POSITIVE") Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Telugu Headline
                Text(
                    text = art.teluguTitle ?: art.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Original English Headline
                Text(
                    text = "Original: ${art.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Telugu Summary / Description
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "తెలుగు వివరణ (Telugu Translation)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = art.teluguDescription ?: art.description ?: "",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Telugu translation generated by NewsTelugu.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Source Attribution Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Source: ${art.sourceName}", style = MaterialTheme.typography.titleSmall)
                        Text(text = "Published: ${art.publishedAt}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Read Original Article & Share Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(art.sourceUrl))
                            context.startActivity(browserIntent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Read Original Article")
                    }

                    OutlinedButton(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "${art.teluguTitle}\n\nRead original: ${art.sourceUrl}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Article"))
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                    }
                }
            }
        }
    }
}
