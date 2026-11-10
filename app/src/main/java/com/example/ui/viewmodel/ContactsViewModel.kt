package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ContactEntity
import com.example.data.repository.ContactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModel(private val contactRepo: ContactRepository) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val contacts: StateFlow<List<ContactEntity>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) contactRepo.getAllContacts()
            else contactRepo.searchContacts(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearch(query: String) {
        searchQuery.value = query
    }

    fun saveContact(name: String, phone: String, notes: String, isVip: Boolean, existingId: Long = 0) {
        viewModelScope.launch {
            val contact = ContactEntity(
                id = existingId,
                name = name.trim(),
                phoneNumber = phone.trim(),
                customNotes = notes.trim(),
                isVip = isVip
            )
            contactRepo.saveContact(contact)
        }
    }

    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch { contactRepo.deleteContact(contact) }
    }
}
