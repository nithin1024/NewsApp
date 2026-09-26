package com.newstelugu.app.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.newstelugu.app.core.network.NetworkClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToLanguage: () -> Unit,
    onNavigateToTheme: () -> Unit,
    onNavigateToPersonalization: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToDisclaimer: () -> Unit
) {
    var showServerDialog by remember { mutableStateOf(false) }
    var serverUrlText by remember { mutableStateOf(NetworkClient.getBaseUrl()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚙️ Settings & Options", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingsItemCard("🖥️ Server URL Configuration", NetworkClient.getBaseUrl(), Icons.Default.Dns) {
                serverUrlText = NetworkClient.getBaseUrl()
                showServerDialog = true
            }
            SettingsItemCard("🌐 Language Settings", "Telugu / English", Icons.Default.Language, onNavigateToLanguage)
            SettingsItemCard("🎨 Theme Settings", "Light / Dark / System Theme", Icons.Default.Palette, onNavigateToTheme)
            SettingsItemCard("⭐ Personalization", "Favorite Categories, Companies & Sectors", Icons.Default.Tune, onNavigateToPersonalization)
            SettingsItemCard("🔔 Notification Settings", "Categorized FCM Push Notifications", Icons.Default.Notifications, onNavigateToNotifications)
            SettingsItemCard("🚨 Alerts Management", "Price & Technical Indicator Alerts", Icons.Default.NotificationsActive, onNavigateToAlerts)
            SettingsItemCard("ℹ️ About NewsTelugu", "App Version & Mission", Icons.Default.Info, onNavigateToAbout)
            SettingsItemCard("📜 Financial Disclaimer", "Important Informational Notice", Icons.Default.Gavel, onNavigateToDisclaimer)
        }

        if (showServerDialog) {
            AlertDialog(
                onDismissRequest = { showServerDialog = false },
                title = { Text("Configure Backend Server URL") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Enter your hosted production server URL or local address:", style = MaterialTheme.typography.bodySmall)
                        OutlinedTextField(
                            value = serverUrlText,
                            onValueChange = { serverUrlText = it },
                            label = { Text("Server URL") },
                            placeholder = { Text("https://your-backend.onrender.com/") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (serverUrlText.isNotBlank()) {
                            NetworkClient.setBaseUrl(serverUrlText)
                            showServerDialog = false
                        }
                    }) {
                        Text("Save & Apply")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showServerDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun SettingsItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}
