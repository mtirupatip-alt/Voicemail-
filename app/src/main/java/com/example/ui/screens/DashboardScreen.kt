package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onRequestRole: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshRoleStatus(context)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(AccentIndigo),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = "Receptionist Logo",
                            tint = TextPrimaryDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Offline AI Receptionist",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = "Private • Local Telephony Guard",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                        )
                    }
                }
                IconButton(
                    onClick = onNavigateToSettings,
                    modifier = Modifier.testTag("quick_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TextSecondaryDark
                    )
                }
            }
        }

        // Android Telecom Role Warning Card (if not granted)
        if (!uiState.isCallScreeningRoleHeld) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentWarning.copy(alpha = 0.15f)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AccentWarning)),
                    modifier = Modifier.fillMaxWidth().testTag("role_warning_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = "Warning", tint = AccentWarning)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Role Required for Screening",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Android requires setting this app as your Call Screening service to detect incoming callers and log them offline.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onRequestRole,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentWarning),
                            modifier = Modifier.testTag("grant_screening_role_button")
                        ) {
                            Text("Set as Call Screening App", color = NavyDarkBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Status Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Receptionist Engine Status", color = TextSecondaryDark, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (uiState.isServiceActive && uiState.isCallScreeningRoleHeld) "Active & Guarding Calls" else "Service Paused / Role Needed",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (uiState.isServiceActive && uiState.isCallScreeningRoleHeld) AccentSuccess else AccentWarning
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (uiState.isCallScreeningRoleHeld) "Role Granted • Safe Non-blocking Mode" else "Role not set in Android settings",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(if (uiState.isServiceActive && uiState.isCallScreeningRoleHeld) AccentSuccess else AccentWarning)
                    )
                }
            }
        }

        // Stats Counters
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Screened Today", color = TextSecondaryDark, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "${uiState.todayDetectedCalls}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp,
                            color = AccentIndigo
                        )
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Logged", color = TextSecondaryDark, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "${uiState.totalDetectedCalls}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp,
                            color = AccentPurple
                        )
                    }
                }
            }
        }

        // Recent Calls Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Screened Calls",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(
                    onClick = onNavigateToHistory,
                    modifier = Modifier.testTag("view_all_history_button")
                ) {
                    Text("View All", color = AccentIndigo)
                }
            }
        }

        // Recent Calls List
        if (uiState.recentCalls.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CallReceived, contentDescription = null, tint = TextSecondaryDark, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No calls screened yet.", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
                            Text("When someone calls, their info will appear here.", fontSize = 12.sp, color = TextSecondaryDark)
                        }
                    }
                }
            }
        } else {
            items(uiState.recentCalls, key = { it.id }) { record ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().testTag("recent_call_item_${record.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhoneCallback, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = record.matchedName ?: record.rawNumber,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            if (record.matchedName != null) {
                                Text(record.rawNumber, style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            Text(formatter.format(Date(record.timestamp)), style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)
                            Text(record.screeningAction, style = MaterialTheme.typography.labelSmall, color = AccentIndigo)
                        }
                    }
                }
            }
        }
    }
}
