package com.newstelugu.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.newstelugu.app.presentation.alerts.AlertsScreen
import com.newstelugu.app.presentation.article.ArticleDetailsScreen
import com.newstelugu.app.presentation.home.HomeScreen
import com.newstelugu.app.presentation.markets.GlobalMarketsScreen
import com.newstelugu.app.presentation.markets.MarketsScreen
import com.newstelugu.app.presentation.news.NewsCategoriesScreen
import com.newstelugu.app.presentation.news.NewsListScreen
import com.newstelugu.app.presentation.notifications.NotificationHistoryScreen
import com.newstelugu.app.presentation.notifications.NotificationSettingsScreen
import com.newstelugu.app.presentation.search.GlobalSearchScreen
import com.newstelugu.app.presentation.settings.*
import com.newstelugu.app.presentation.stock.StockDetailsScreen
import com.newstelugu.app.presentation.stock.StockSearchScreen
import com.newstelugu.app.presentation.watchlist.WatchlistScreen

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Markets,
    Screen.News,
    Screen.Watchlist,
    Screen.More
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTeluguAppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (bottomNavItems.any { it.route == currentRoute }) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            selected = (currentRoute == screen.route),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                            label = { Text(screen.title) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            // Bottom Bar Screens
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToArticle = { articleId -> navController.navigate(Screen.ArticleDetails.createRoute(articleId)) },
                    onNavigateToStock = { symbol -> navController.navigate(Screen.StockDetails.createRoute(symbol)) },
                    onNavigateToSearch = { navController.navigate(Screen.GlobalSearch.route) }
                )
            }

            composable(Screen.Markets.route) {
                MarketsScreen(
                    onNavigateToStock = { symbol -> navController.navigate(Screen.StockDetails.createRoute(symbol)) },
                    onNavigateToGlobalMarkets = { navController.navigate(Screen.GlobalMarkets.route) }
                )
            }

            composable(Screen.News.route) {
                NewsCategoriesScreen(
                    onSelectCategory = { cat -> navController.navigate("news_list/$cat") }
                )
            }

            composable(Screen.Watchlist.route) {
                WatchlistScreen(
                    onNavigateToStock = { symbol -> navController.navigate(Screen.StockDetails.createRoute(symbol)) },
                    onNavigateToSearch = { navController.navigate(Screen.StockSearch.route) }
                )
            }

            composable(Screen.More.route) {
                SettingsScreen(
                    onNavigateToLanguage = { navController.navigate(Screen.LanguageSettings.route) },
                    onNavigateToTheme = { navController.navigate(Screen.ThemeSettings.route) },
                    onNavigateToPersonalization = { navController.navigate(Screen.Personalization.route) },
                    onNavigateToNotifications = { navController.navigate(Screen.NotificationSettings.route) },
                    onNavigateToAlerts = { navController.navigate(Screen.Alerts.route) },
                    onNavigateToAbout = { navController.navigate(Screen.About.route) },
                    onNavigateToDisclaimer = { navController.navigate(Screen.FinancialDisclaimer.route) }
                )
            }

            // Secondary & Detail Screens
            composable("news_list/{category}") { backStackEntry ->
                val cat = backStackEntry.arguments?.getString("category") ?: "STOCK MARKET"
                NewsListScreen(
                    category = cat,
                    onNavigateToArticle = { articleId -> navController.navigate(Screen.ArticleDetails.createRoute(articleId)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.ArticleDetails.route,
                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
                ArticleDetailsScreen(
                    articleId = articleId,
                    onNavigateToStock = { symbol -> navController.navigate(Screen.StockDetails.createRoute(symbol)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.GlobalMarkets.route) {
                GlobalMarketsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.StockSearch.route) {
                StockSearchScreen(
                    onSelectStock = { symbol -> navController.navigate(Screen.StockDetails.createRoute(symbol)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.StockDetails.route,
                arguments = listOf(navArgument("symbol") { type = NavType.StringType })
            ) { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
                StockDetailsScreen(
                    symbol = symbol,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Alerts.route) {
                AlertsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.NotificationHistory.route) {
                NotificationHistoryScreen(
                    onNavigateToArticle = { articleId -> navController.navigate(Screen.ArticleDetails.createRoute(articleId)) },
                    onNavigateToStock = { symbol -> navController.navigate(Screen.StockDetails.createRoute(symbol)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.NotificationSettings.route) {
                NotificationSettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.GlobalSearch.route) {
                GlobalSearchScreen(
                    onNavigateToStock = { symbol -> navController.navigate(Screen.StockDetails.createRoute(symbol)) },
                    onNavigateToArticle = { articleId -> navController.navigate(Screen.ArticleDetails.createRoute(articleId)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.LanguageSettings.route) {
                LanguageSettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.ThemeSettings.route) {
                ThemeSettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.Personalization.route) {
                PersonalizationScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.About.route) {
                AboutScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.FinancialDisclaimer.route) {
                DisclaimerScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
