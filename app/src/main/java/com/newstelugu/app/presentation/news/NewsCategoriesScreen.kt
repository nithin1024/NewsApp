package com.newstelugu.app.presentation.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val ALL_CATEGORIES = listOf(
    "Top Stories", "Breaking News", "India", "World", "Business",
    "Banking", "Stock Market", "Economy", "Technology", "Startups",
    "Science", "Health", "Sports", "Entertainment", "Politics",
    "Telangana", "Andhra Pradesh", "Crypto/Web3", "Personal Finance"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsCategoriesScreen(
    onSelectCategory: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📂 News Categories", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(ALL_CATEGORIES) { cat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable { onSelectCategory(cat) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
