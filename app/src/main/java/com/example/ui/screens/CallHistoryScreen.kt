package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.local.CallRecordEntity
import com.example.ui.theme.AccentIndigo
import com.example.ui.theme.AccentSuccess
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.CallHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CallHistoryScreen(viewModel: CallHistoryViewModel) {
    val records by viewModel.callRecords.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showClearConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Call Records",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    "${records.size} total screened entries",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                )
            }
            if (records.isNotEmpty()) {
                IconButton(
                    onClick = { showClearConfirm = true },
                    modifier = Modifier.testTag("clear_all_history_button")
                ) {
                    Icon(
                        Icons.Default.DeleteSweep,
                        contentDescription = "Clear All Records",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearch(it) },
            modifier = Modifier.fillMaxWidth().testTag("history_search_input"),
            placeholder = { Text("Search caller number or contact name...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearch("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear Search")
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.SearchOff, contentDescription = null, tint = TextSecondaryDark, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        if (searchQuery.isNotBlank()) "No records found matching '$searchQuery'" else "No screened calls recorded yet.",
                        color = TextSecondaryDark
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(records, key = { it.id }) { record ->
                    HistoryItemCard(record = record, onDelete = { viewModel.deleteRecord(record) })
                }
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Clear All Call Records?") },
            text = { Text("This will permanently delete all call detection records stored in the offline local database.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearConfirm = false
                    },
                    modifier = Modifier.testTag("confirm_clear_history_button")
                ) {
                    Text("Delete All", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun HistoryItemCard(record: CallRecordEntity, onDelete: () -> Unit) {
    val dateStr = remember(record.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
        sdf.format(Date(record.timestamp))
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth().testTag("call_item_${record.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.matchedName ?: record.rawNumber,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (record.matchedName != null) {
                    Text(record.rawNumber, style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(dateStr, style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Action: ${record.screeningAction}", style = MaterialTheme.typography.labelSmall, color = AccentIndigo)
                    if (record.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("• ${record.notes}", style = MaterialTheme.typography.labelSmall, color = AccentSuccess)
                    }
                }
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_record_${record.id}")
            ) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Record", tint = TextSecondaryDark)
            }
        }
    }
}
