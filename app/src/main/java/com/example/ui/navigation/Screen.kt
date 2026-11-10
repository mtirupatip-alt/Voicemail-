package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    data object Dashboard : Screen("dashboard", "Dashboard")
    data object History : Screen("history", "Call History")
    data object Contacts : Screen("contacts", "Contacts CRM")
    data object Settings : Screen("settings", "Settings")
    data object AiReadiness : Screen("ai_readiness", "AI & Privacy")
}
