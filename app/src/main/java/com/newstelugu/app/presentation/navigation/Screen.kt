package com.newstelugu.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    // Bottom Bar Items
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Markets : Screen("markets", "Markets", Icons.Default.ShowChart)
    object News : Screen("news", "News", Icons.Default.Article)
    object Watchlist : Screen("watchlist", "Watchlist", Icons.Default.Star)
    object More : Screen("more", "More", Icons.Default.Menu)

    // Feature Screens
    object GlobalMarkets : Screen("global_markets", "Global Markets")
    object ArticleDetails : Screen("article_details/{articleId}") {
        fun createRoute(articleId: String) = "article_details/$articleId"
    }
    object StockSearch : Screen("stock_search", "Stock Search")
    object StockDetails : Screen("stock_details/{symbol}") {
        fun createRoute(symbol: String) = "stock_details/$symbol"
    }
    object Alerts : Screen("alerts", "Alerts")
    object NotificationHistory : Screen("notification_history", "Notification History")
    object NotificationSettings : Screen("notification_settings", "Notification Settings")
    object GlobalSearch : Screen("global_search", "Search")
    object LanguageSettings : Screen("language_settings", "Language Settings")
    object ThemeSettings : Screen("theme_settings", "Theme Settings")
    object Personalization : Screen("personalization", "Personalization")
    object About : Screen("about", "About NewsTelugu")
    object FinancialDisclaimer : Screen("disclaimer", "Financial Disclaimer")
}
