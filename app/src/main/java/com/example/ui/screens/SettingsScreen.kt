package com.example.ui.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onRequestRole: () -> Unit,
    onRequestNotifications: () -> Unit
) {
    val isServiceActive by viewModel.isServiceActive.collectAsState()
    val areNotificationsEnabled by viewModel.areNotificationsEnabled.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    var showWipeConfirm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Receptionist Settings",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                "Configure offline screening, permissions, and theme",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
            )
        }

        // Service Switch
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Screening Service Active", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                        Text("Enable or pause local incoming call screening", style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)
                    }
                    Switch(
                        checked = isServiceActive,
                        onCheckedChange = { viewModel.toggleService(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentIndigo),
                        modifier = Modifier.testTag("service_active_toggle")
                    )
                }
            }
        }

        // Role & Permission Triggers
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Android System Permissions", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

                    OutlinedButton(
                        onClick = onRequestRole,
                        modifier = Modifier.fillMaxWidth().testTag("settings_request_role_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Configure Call Screening Role")
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        OutlinedButton(
                            onClick = onRequestNotifications,
                            modifier = Modifier.fillMaxWidth().testTag("settings_request_notifications_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Request Notification Permission")
                        }
                    }
                }
            }
        }

        // Notifications Preference Switch
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Screening Notifications", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                        Text("Show a heads-up alert whenever a call is logged", style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)
                    }
                    Switch(
                        checked = areNotificationsEnabled,
                        onCheckedChange = { viewModel.toggleNotifications(it) },
                        modifier = Modifier.testTag("notifications_toggle")
                    )
                }
            }
        }

        // Theme Selection
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Interface Theme", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("DARK", "LIGHT").forEach { mode ->
                            FilterChip(
                                selected = themeMode == mode,
                                onClick = { viewModel.setTheme(mode) },
                                label = { Text(mode.lowercase().replaceFirstChar { it.uppercase() }) },
                                modifier = Modifier.testTag("theme_chip_$mode")
                            )
                        }
                    }
                }
            }
        }

        // Data Management
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Local Data Management", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("All call records and contacts are stored strictly in private device storage. No data ever leaves this phone.", style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showWipeConfirm = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("wipe_data_button")
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Purge Local History")
                    }
                }
            }
        }

        // Version & Privacy Stamp
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Offline AI Receptionist v1.0.0", fontSize = 12.sp, color = TextSecondaryDark)
                Text("Zero Network Permissions • 100% Offline Privacy", fontSize = 11.sp, color = AccentSuccess)
            }
        }
    }

    if (showWipeConfirm) {
        AlertDialog(
            onDismissRequest = { showWipeConfirm = false },
            title = { Text("Purge History Database?") },
            text = { Text("This will permanently delete all screened call history from this phone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.wipeLocalCallData()
                        showWipeConfirm = false
                    },
                    modifier = Modifier.testTag("confirm_wipe_data_button")
                ) {
                    Text("Confirm Purge", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWipeConfirm = false }) { Text("Cancel") }
            }
        )
    }
}
