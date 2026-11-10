package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AiReadinessScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "AI Receptionist Architecture",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                "Offline privacy guarantees and future AI extension roadmap",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
            )
        }

        // Technical Transparency Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = AccentSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Android Platform Cellular Audio Policy", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Android security sandboxing (from Android 10 up to Android 15) strictly prohibits normal third-party apps from capturing two-way voice audio or speaking back to callers into standard cellular SIM calls (Jio, Airtel, Vi, etc.).\n\n" +
                                "To achieve full automatic conversational voice answering, supported platforms use VoIP/SIP gateways where voice streaming is fully controllable, coupled with on-device Whisper STT and local LLM inference engines.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Modular Roadmap Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Offline AI Modules Status", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

                    ModuleStatusRow(
                        title = "Call Screening Service",
                        detail = "Active (Official Android API)",
                        isReady = true
                    )
                    ModuleStatusRow(
                        title = "Local CRM & VIP Contact Matching",
                        detail = "Active (Room SQLite)",
                        isReady = true
                    )
                    ModuleStatusRow(
                        title = "Zero-Network Data Sandbox",
                        detail = "Active (No Internet Permission Declared)",
                        isReady = true
                    )
                    ModuleStatusRow(
                        title = "On-Device Whisper STT / Speech-to-Text",
                        detail = "Architecture Prepared (Sherpa-ONNX / TFLite)",
                        isReady = false
                    )
                    ModuleStatusRow(
                        title = "On-Device Small LLM Inference",
                        detail = "Architecture Prepared (Gemma 2B / MediaPipe)",
                        isReady = false
                    )
                    ModuleStatusRow(
                        title = "Enterprise SIP/VoIP Audio Channel",
                        detail = "Supported Pathway for Live Voice Dialogues",
                        isReady = false
                    )
                }
            }
        }

        // Privacy Guarantees
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Privacy & Security Commitments", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• No audio recording or surreptitious microphone tapping.\n" +
                                "• No cloud telemetry or third-party tracking SDKs.\n" +
                                "• Database is private to the application sandbox storage.\n" +
                                "• User retains 100% control to purge all screening records.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ModuleStatusRow(title: String, detail: String, isReady: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
            Text(detail, fontSize = 12.sp, color = TextSecondaryDark)
        }
        AssistChip(
            onClick = {},
            label = { Text(if (isReady) "ACTIVE" else "ROADMAP", fontSize = 10.sp) },
            colors = AssistChipDefaults.assistChipColors(
                labelColor = if (isReady) AccentSuccess else AccentWarning
            )
        )
    }
}
