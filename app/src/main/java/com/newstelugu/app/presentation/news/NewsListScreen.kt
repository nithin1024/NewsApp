package com.newstelugu.app.presentation.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newstelugu.app.core.ui.components.ErrorState
import com.newstelugu.app.core.ui.components.NewsCard
import com.newstelugu.app.core.ui.components.ShimmerLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    category: String,
    onNavigateToArticle: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: NewsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(category) {
        viewModel.loadNewsForCategory(category)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.padding(padding)) { ShimmerLoader() }
        } else if (state.error != null) {
            ErrorState(message = state.error!!, onRetry = { viewModel.loadNewsForCategory(category) })
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.articles) { article ->
                    NewsCard(article = article)
                }
            }
        }
    }
}
