package com.newstelugu.app.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(onBack: () -> Unit) {
    var selectedTheme by remember { mutableStateOf("system") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎨 Theme Settings", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Choose Application Theme", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            ThemeOptionRow("📱 System Default", selectedTheme == "system") { selectedTheme = "system" }
            ThemeOptionRow("☀️ Light Mode", selectedTheme == "light") { selectedTheme = "light" }
            ThemeOptionRow("🌙 Dark Mode", selectedTheme == "dark") { selectedTheme = "dark" }
        }
    }
}

@Composable
fun ThemeOptionRow(title: String, isSelected: Boolean, onSelect: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            RadioButton(selected = isSelected, onClick = onSelect)
        }
    }
}
