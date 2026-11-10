package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CallRecordEntity
import com.example.data.repository.CallRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CallHistoryViewModel(private val callRepo: CallRepository) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val callRecords: StateFlow<List<CallRecordEntity>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) callRepo.getAllRecords()
            else callRepo.searchRecords(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearch(query: String) {
        searchQuery.value = query
    }

    fun deleteRecord(record: CallRecordEntity) {
        viewModelScope.launch { callRepo.deleteRecord(record) }
    }

    fun clearAllHistory() {
        viewModelScope.launch { callRepo.clearHistory() }
    }
}
