package com.example.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.compose.*
import com.example.ReceptionistApp
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.*

@Composable
fun MainContainerScreen(
    onRequestRole: () -> Unit,
    onRequestNotifications: () -> Unit
) {
    val navController = rememberNavController()
    val app = ReceptionistApp.instance

    val dashboardVm = remember { DashboardViewModel(app.callRepository, app.preferencesRepository) }
    val historyVm = remember { CallHistoryViewModel(app.callRepository) }
    val contactsVm = remember { ContactsViewModel(app.contactRepository) }
    val settingsVm = remember { SettingsViewModel(app.preferencesRepository, app.callRepository) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == Screen.Dashboard.route,
                    onClick = { navController.navigate(Screen.Dashboard.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Home") },
                    modifier = Modifier.testTag("nav_tab_dashboard")
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.History.route,
                    onClick = { navController.navigate(Screen.History.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Call, contentDescription = "History") },
                    label = { Text("History") },
                    modifier = Modifier.testTag("nav_tab_history")
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Contacts.route,
                    onClick = { navController.navigate(Screen.Contacts.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.People, contentDescription = "Contacts") },
                    label = { Text("Contacts") },
                    modifier = Modifier.testTag("nav_tab_contacts")
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Settings.route,
                    onClick = { navController.navigate(Screen.Settings.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    modifier = Modifier.testTag("nav_tab_settings")
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.AiReadiness.route,
                    onClick = { navController.navigate(Screen.AiReadiness.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.SmartToy, contentDescription = "AI & Privacy") },
                    label = { Text("AI / Policy") },
                    modifier = Modifier.testTag("nav_tab_ai_readiness")
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = dashboardVm,
                    onRequestRole = onRequestRole,
                    onNavigateToHistory = { navController.navigate(Screen.History.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            composable(Screen.History.route) {
                CallHistoryScreen(viewModel = historyVm)
            }
            composable(Screen.Contacts.route) {
                ContactsScreen(viewModel = contactsVm)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingsVm,
                    onRequestRole = onRequestRole,
                    onRequestNotifications = onRequestNotifications
                )
            }
            composable(Screen.AiReadiness.route) {
                AiReadinessScreen()
            }
        }
    }
}
