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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.ContactEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.ContactsViewModel

@Composable
fun ContactsScreen(viewModel: ContactsViewModel) {
    val contacts by viewModel.contacts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var editingContact by remember { mutableStateOf<ContactEntity?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingContact = null
                    showDialog = true
                },
                containerColor = AccentIndigo,
                modifier = Modifier.testTag("add_contact_fab")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add Contact", tint = TextPrimaryDark)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Local Contacts CRM (${contacts.size})",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                "VIP rules and offline recognition for incoming calls",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearch(it) },
                modifier = Modifier.fillMaxWidth().testTag("contacts_search_input"),
                placeholder = { Text("Search contact name or number...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (contacts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PeopleOutline, contentDescription = null, tint = TextSecondaryDark, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No contacts saved yet.", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
                        Text("Tap + below to add VIP contacts or custom receptionist notes.", color = TextSecondaryDark)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(contacts, key = { it.id }) { contact ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth().testTag("contact_item_${contact.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(if (contact.isVip) AccentWarning else AccentIndigo),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        contact.name.take(1).uppercase(),
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(contact.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                                        if (contact.isVip) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("VIP", style = MaterialTheme.typography.labelSmall, color = AccentWarning)
                                        }
                                    }
                                    Text(contact.phoneNumber, style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)
                                    if (contact.customNotes.isNotBlank()) {
                                        Text("Notes: ${contact.customNotes}", style = MaterialTheme.typography.bodySmall, color = AccentCyan)
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        editingContact = contact
                                        showDialog = true
                                    },
                                    modifier = Modifier.testTag("edit_contact_${contact.id}")
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondaryDark)
                                }
                                IconButton(
                                    onClick = { viewModel.deleteContact(contact) },
                                    modifier = Modifier.testTag("delete_contact_${contact.id}")
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextSecondaryDark)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ContactFormDialog(
            contact = editingContact,
            onDismiss = { showDialog = false },
            onSave = { name, phone, notes, isVip, id ->
                viewModel.saveContact(name, phone, notes, isVip, id)
                showDialog = false
            }
        )
    }
}

@Composable
fun ContactFormDialog(
    contact: ContactEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String, notes: String, isVip: Boolean, id: Long) -> Unit
) {
    var name by remember { mutableStateOf(contact?.name ?: "") }
    var phone by remember { mutableStateOf(contact?.phoneNumber ?: "") }
    var notes by remember { mutableStateOf(contact?.customNotes ?: "") }
    var isVip by remember { mutableStateOf(contact?.isVip ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (contact == null) "Add Offline Contact" else "Edit Contact") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    modifier = Modifier.testTag("contact_name_input")
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    modifier = Modifier.testTag("contact_phone_input")
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Receptionist Priority / Notes") },
                    modifier = Modifier.testTag("contact_notes_input")
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isVip,
                        onCheckedChange = { isVip = it },
                        modifier = Modifier.testTag("contact_vip_checkbox")
                    )
                    Text("Mark as VIP Contact")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onSave(name, phone, notes, isVip, contact?.id ?: 0L)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentIndigo),
                modifier = Modifier.testTag("save_contact_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
